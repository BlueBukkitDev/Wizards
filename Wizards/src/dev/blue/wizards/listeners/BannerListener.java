package dev.blue.wizards.listeners;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import dev.blue.wizards.Main;
import dev.blue.wizards.spells.EnergySpell;
import dev.blue.wizards.spells.FireballSpell;
import dev.blue.wizards.spells.GustSpell;
import dev.blue.wizards.spells.HealSpell;
import dev.blue.wizards.spells.HideSpell;
import dev.blue.wizards.spells.LeapSpell;
import dev.blue.wizards.spells.LevitateSpell;
import dev.blue.wizards.spells.LightningSpell;
import dev.blue.wizards.spells.Spell;

public class BannerListener implements Listener {
	private Main main;
	public BannerListener(Main main) {
		this.main = main;
	}
	@EventHandler
	public void onCollect(PlayerMoveEvent e) {
		if(e.getPlayer().getGameMode() != main.playMode) {
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
			if(cloud.getBasePotionType().equals(PotionType.HARMING)) {
				damaged.setFireTicks(damaged.getFireTicks()+(new FireballSpell(main, (Player)cloud.getSource()).getLevel())*20);
			}
		}
		if(damager instanceof ShulkerBullet) {
			ShulkerBullet tooth = (ShulkerBullet) damager;
			if(tooth.getPersistentDataContainer().has(new LevitateSpell(main, null).getKey(), PersistentDataType.BYTE)) {
				if(damaged instanceof Player) {
					Player dmgd = (Player) damaged;
					double level = new LevitateSpell(main, (Player)tooth.getShooter()).getLevel();
					dmgd.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30+(int)(5*level), 1+(int)(level/10)));
					dmgd.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30+(int)(5*level), 2+(int)(level/5)));
					dmgd.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40+(int)(5*level), 1+(int)(level/5)));
				}
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
		
		if(p.getGameMode() != main.playMode) {
			return;
		}
		if(e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		if(e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}
		ItemStack hand = p.getInventory().getItemInMainHand();
		if(hand == null || hand.getType() == Material.AIR) {
			return;
		}
		getSpellByWand(hand, p).cast();
	}
	
	private Spell getSpellByWand(ItemStack stack, Player holder) {
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-energySpell"))){
			return new EnergySpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-healSpell"))){
			return new HealSpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-lightningSpell"))){
			return new LightningSpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-leapSpell"))){
			return new LeapSpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-gustSpell"))){
			return new GustSpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-fireballSpell"))){
			return new FireballSpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-levitateSpell"))){
			return new LevitateSpell(main, holder);
		}
		if(stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "wizards-hideSpell"))){
			return new HideSpell(main, holder);
		}
		return null;
	}
	
	@EventHandler
	public void onCloseClick(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		e.setCancelled(true);
		Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(p, Action.RIGHT_CLICK_AIR , p.getInventory().getItemInMainHand(), null, null, EquipmentSlot.HAND));
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
