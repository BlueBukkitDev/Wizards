package dev.blue.wizards.listeners;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.blue.wizards.Main;
import dev.blue.wizards.spells.EnergySpell;
import dev.blue.wizards.spells.FireballSpell;
import dev.blue.wizards.spells.GustSpell;
import dev.blue.wizards.spells.HealSpell;
import dev.blue.wizards.spells.LeapSpell;
import dev.blue.wizards.spells.LightningSpell;
import dev.blue.wizards.wands.EnergyWand;
import dev.blue.wizards.wands.FireballWand;
import dev.blue.wizards.wands.GustWand;
import dev.blue.wizards.wands.HealWand;
import dev.blue.wizards.wands.LeapWand;
import dev.blue.wizards.wands.LightningWand;

public class BannerListener implements Listener {
	private Main main;
	private List<Player> clickCooldown;
	public BannerListener(Main main) {
		this.main = main;
		clickCooldown = new ArrayList<Player>();
	}
	@EventHandler
	public void onCollect(PlayerMoveEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		if(e.getFrom().getBlock().equals(e.getTo().getBlock())) {
			return;
		}
		if(!(e.getTo().getBlock().getState() instanceof Banner || e.getTo().getBlock().getRelative(BlockFace.DOWN).getState() instanceof Banner)) {
			return;
		}
		Banner banner;
		if(e.getTo().getBlock().getState() instanceof Banner) {
			banner = (Banner) e.getTo().getBlock().getState();
		}else banner = (Banner) e.getTo().getBlock().getRelative(BlockFace.DOWN).getState();
		if(!banner.getPersistentDataContainer().has(main.getUtils().key_spellType, PersistentDataType.BYTE)){
			return;
		}
		main.getUtils().detectSpellAndApply(e.getPlayer(), banner);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onFoodChange(FoodLevelChangeEvent e) {
		if(e.isCancelled()) {
			Player p = (Player)e.getEntity();
			if(main.getUtils().foodCooldown.containsKey(p)) {
				if(main.getUtils().foodCooldown.get(p) == 0) {
					main.getUtils().foodCooldown.remove(p);
					e.setCancelled(false);
				}else main.getUtils().foodCooldown.put(p, main.getUtils().foodCooldown.get(p)-1);
			}else {
				main.getUtils().foodCooldown.put(p, 8);
			}
		}
	}
	
	@EventHandler
	public void onHitGround(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		BigDecimal big = BigDecimal.valueOf(p.getLocation().getY());
		big = big.subtract(new BigDecimal(big.intValue()));
		Double yDecimal = big.doubleValue();
		if (!p.getLocation().getBlock().getRelative(BlockFace.DOWN).isPassable() && yDecimal < 0.1) {
			main.getUtils().resetLeaps(p);
		}
	}
	
	@EventHandler
	public void onFireball(ProjectileHitEvent e) {
		Projectile ent = e.getEntity();
		if(!(ent instanceof Fireball)) {
			return;
		}
		e.setCancelled(true);
		((Fireball)ent).remove();
		new FireballSpell(main, (Player)ent.getShooter()).runImpact((Fireball)ent);
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		if(e.getCause() == DamageCause.FIRE_TICK) {
			main.getUtils().resetCombatTimer((Player)e.getEntity());
			return;
		}
	}
	
	@EventHandler
	public void onMagicHit(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damaged = e.getEntity();
		if(damager instanceof Fireball) {
			e.setCancelled(true);
		}
		if(damager instanceof Player) {
			if(e.getCause() == DamageCause.ENTITY_ATTACK) {
				Bukkit.getPluginManager().callEvent(new PlayerInteractEvent((Player) damager, Action.LEFT_CLICK_AIR , null, null, null, EquipmentSlot.HAND));
				e.setCancelled(true);
			}
		}
		if(damager instanceof Snowball) {
			Snowball snowball = (Snowball) damager;
			if(snowball.isGlowing()) {
				e.setDamage(4+(new EnergySpell(main, (Player)snowball.getShooter()).getLevel()/2.5));
			}
			if(snowball.getPersistentDataContainer().has(new GustSpell(main, null).getKey(), PersistentDataType.BYTE)) {
				e.setDamage(0);//May have repaired this to some degree; now reduce increase per level. 
				double level = new GustSpell(main, (Player)snowball.getShooter()).getLevel();
				double nerf = 7;//lower to nerf more
				damaged.setVelocity(snowball.getVelocity().multiply(1.8+(level/nerf)).setY(0.5));
			}
		}
		if(damager instanceof AreaEffectCloud) {
			AreaEffectCloud cloud = (AreaEffectCloud) damager;
			if(cloud.getBasePotionData().equals(new PotionData(PotionType.INSTANT_DAMAGE, false, false))) {
				damaged.setFireTicks(damaged.getFireTicks()+(new FireballSpell(main, (Player)cloud.getSource()).getLevel())*40);
			}
		}
	}
	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) {
			return;
		}
		if(e.getAction() == InventoryAction.PICKUP_SOME || e.getAction() == InventoryAction.CLONE_STACK || e.getAction() == InventoryAction.DROP_ONE_CURSOR || 
			e.getAction() == InventoryAction.DROP_ONE_SLOT || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction() == InventoryAction.HOTBAR_SWAP || 
			e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PICKUP_ONE || e.getAction() == InventoryAction.PLACE_ONE || 
			e.getAction() == InventoryAction.PLACE_SOME || e.getAction() == InventoryAction.UNKNOWN) {
			e.setCancelled(true);
		}
		if(!e.getCurrentItem().hasItemMeta()) {
			return;
		}
		if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(main.getUtils().key_gui, PersistentDataType.BYTE)) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onClick(PlayerInteractEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		p.swingMainHand();
		if(p.getGameMode() != GameMode.ADVENTURE && e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		if(e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if(clickCooldown.contains(e.getPlayer())) {
			return;
		}
		clickCooldown.add(p);
		ItemStack hand = p.getInventory().getItemInMainHand();
		if(hand == null || hand.getType() == Material.AIR) {
			return;
		}
		if(hand.isSimilar(new EnergyWand(main).getItemStack())) {
			new EnergySpell(main, p).cast();
		}else if(hand.isSimilar(new GustWand(main).getItemStack())) {
			new GustSpell(main, p).cast();
		}else if(hand.isSimilar(new LightningWand(main).getItemStack())) {
			new LightningSpell(main, p).cast();
		}else if(hand.isSimilar(new FireballWand(main).getItemStack())) {
			new FireballSpell(main, p).cast();
		}else if(hand.isSimilar(new HealWand(main).getItemStack())) {
			new HealSpell(main, p).cast();
		}else if(hand.isSimilar(new LeapWand(main).getItemStack())) {
			new LeapSpell(main, p).cast();
		}
		BukkitRunnable cooldown = new BukkitRunnable() {
			@Override
			public void run() {
				clickCooldown.remove(p);
			}
		};
		cooldown.runTaskLater(main, 1);
	}
	
	@EventHandler
	public void onCloseClick(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		e.setCancelled(true);
		Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(p, Action.RIGHT_CLICK_AIR , null, null, null, EquipmentSlot.HAND));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(!main.getGame().isContestant(e.getPlayer())) {
			Bukkit.broadcastMessage("§7Not a contestant");
			main.getUtils().resetPots(e.getPlayer());
			main.getUtils().resetAllSpells(e.getPlayer());
			main.getUtils().setupInventory(e.getPlayer());
		}
	}
}
