package dev.blue.wizards.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.blue.brawl.events.GameDamageEvent;
import dev.blue.brawl.events.GameTerminateEvent;
import dev.blue.brawl.events.PlayerCombatEvent;
import dev.blue.brawl.events.PlayerEliminateEvent;
import dev.blue.wizards.Main;
import dev.blue.wizards.spells.GustSpell;
import dev.blue.wizards.spells.LevitateSpell;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GameListener implements Listener {
	
	private Main main;
	
	public GameListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onEliminate(PlayerEliminateEvent e) {
		e.setRespawnLocation(main.getUtils().getRandomMapSpawn());
		String cause = e.getReason().split("%")[0];
		boolean voidDmg = e.getReason().split("%").length > 1;
		if(e.getKiller() != null) {
			Entity entity = Bukkit.getEntity(e.getKiller());
			if(Bukkit.getEntity(e.getKiller()) instanceof Player) {
				Player killer = (Player) entity;
					if(cause.equalsIgnoreCase("lightning")) {
						e.setDeathMessage("§d"+e.getPlayer().getDisplayName()+" was struck down by "+killer.getDisplayName());
					}else if(cause.equalsIgnoreCase("fire")) {
						e.setDeathMessage("§d"+e.getPlayer().getDisplayName()+" was set ablaze by "+killer.getDisplayName());
					}else if(cause.equalsIgnoreCase("wind")) {
						e.setDeathMessage("§d"+killer.getDisplayName()+" has cast "+e.getPlayer().getDisplayName()+" into the abyss!");
					}else if(cause.equalsIgnoreCase("energy") && !voidDmg) {
						e.setDeathMessage("§d"+killer.getDisplayName()+" wouldn't stop poking "+e.getPlayer().getDisplayName());
					}else if(cause.equalsIgnoreCase("levitate") && !voidDmg) {
						e.setDeathMessage("§d"+killer.getDisplayName()+"'s 'Wingardium Leviosa' was effective against "+e.getPlayer().getDisplayName());
					}else if(cause.equalsIgnoreCase("void") && voidDmg) {
						e.setDeathMessage("§d"+e.getPlayer().getDisplayName()+" dove off the edge to escape "+killer.getDisplayName());
					}else{
						e.setDeathMessage("§d"+killer.getDisplayName()+" killed "+e.getPlayer().getDisplayName());
					}
				}
		}else {
			if(cause.equalsIgnoreCase("lightning")) {
				e.setDeathMessage(main.getMessages().getSelfShockMessage(e.getPlayer()));
			}else if(cause.equalsIgnoreCase("fire")) {
				e.setDeathMessage(main.getMessages().getSelfBurnMessage(e.getPlayer()));
			}else if(cause.equalsIgnoreCase("void")) {
				e.setDeathMessage(main.getMessages().getSuicideMessage(e.getPlayer()));
			}else e.setDeathMessage(main.getMessages().getUnusualDeathMessage(e.getPlayer().getDisplayName()));
		}
		BukkitRunnable setPots = new BukkitRunnable() {
			@Override
			public void run() {
				main.getUtils().resetCombatTimer(e.getPlayer());
				main.getUtils().resetPots(e.getPlayer());
			}
		};
		setPots.runTaskLater(main, 1);
		for(Player each:Bukkit.getOnlinePlayers()) {
			if(each != e.getPlayer()) {
				each.playSound(each, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.4f, 3f);
			}
		}
	}
	
	@EventHandler
	public void onGenericDamage(EntityDamageEvent e) {
		if(e.getCause() != DamageCause.FIRE_TICK) {
			return;
		}
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		String[] colors = {"§4", "§c", "§6"};
		int index = (int) Math.ceil(p.getFireTicks()/20/5);//5 second intervals, being yellow, then red, then dark red. 
		if(index > colors.length-1) {
			index = colors.length-1;
		}
		String heat = colors[index];
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(heat+"ঔঌ "+p.getFireTicks()/20+" ঌঔ"));
	}
	
	@EventHandler
	public void onGameDamage(GameDamageEvent e) {
		e.setSpawnLocation(main.getUtils().getRandomMapSpawn());
	}
	
	@EventHandler
	public void onGameEnd(GameTerminateEvent e) {
		main.getUtils().selectRandomMap();
		e.setSpawnLocation(main.getUtils().getCurrentLobby());
	}
	
	@EventHandler
	public void onCombat(PlayerCombatEvent e) {
		Entity attacker = e.getAttacker();
		if(!(attacker instanceof Player)) {
			if(attacker instanceof Projectile) {
				Projectile proj = (Projectile)attacker;
				if(proj.getShooter() != null && proj.getShooter() instanceof Entity) {
					e.setAttacker((Entity)proj.getShooter());
					if(GustSpell.isGust(proj)) {
						e.setCause("wind");
					}else if(LevitateSpell.isLevitate(proj)) {
						e.setCause("levitate");
					}else {
						e.setCause("energy");
					}
					return;
				}
			}else if(attacker instanceof LightningStrike) {
				e.setAttacker(Bukkit.getPlayer(UUID.fromString(attacker.getCustomName())));
				e.setCause("lightning");
				return;
			}else if(attacker instanceof AreaEffectCloud) {
				e.setAttacker((Entity)((AreaEffectCloud)attacker).getSource());
				e.setCause("fire");
				return;
			}
		}
		e.setAttacker(null);
		return;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().getInventory().clear();
		main.getUtils().resetAllSpells(e.getPlayer());
		main.getUtils().resetCombatTimer(e.getPlayer());
		main.getUtils().resetPots(e.getPlayer());
	}
	
	//@EventHandler
	public void onWin() {
		
	}
}
