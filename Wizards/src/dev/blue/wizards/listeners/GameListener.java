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
import org.bukkit.scheduler.BukkitRunnable;

import dev.blue.brawl.events.PlayerCombatEvent;
import dev.blue.brawl.events.PlayerEliminateEvent;
import dev.blue.wizards.Main;
import dev.blue.wizards.spells.GustSpell;

public class GameListener implements Listener {
	
	private Main main;
	
	String[] selfShockMessages = {
		"§d#name# is conducting themselves well", "§d#name# made a shocking discovery", "§d#name# decided to start lightening up"
	};
	
	String[] selfBurnMessages = {
		"§d#name#'s looking mighty crispy", "§d#name#'s death warrants a study in Spontaneous Human Combustion", "§d#name# is all fired up and ready to go!"
	};
	
	String[] suicideMessages = {
		"§d#name# couldn't take it anymore", "§d#name# was their own worst enemy", "§d#name# found no reason to continue", "§d#name#().setCancelled(true);"
	};
	
	String[] unusualDeathMessages = {
		"§dUnusual circumstances befell #name#", "§d...yeah, #name# does that sometimes", "§dLooks like #name# learned a new trick"
	};
	
	public GameListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onEliminate(PlayerEliminateEvent e) {
		String cause = e.getReason().split("#")[0];
		boolean voidDmg = e.getReason().split("#").length > 1;
		if(e.getKiller() != null) {
			Entity entity = Bukkit.getEntity(e.getKiller());
			if(Bukkit.getEntity(e.getKiller()) instanceof Player) {
				Player killer = (Player) entity;
				if(killer.equals(e.getPlayer())) {
					if(cause.equalsIgnoreCase("lightning")) {
						e.setDeathMessage(main.getMessages().getSelfShockMessage(e.getPlayer()));
					}else if(cause.equalsIgnoreCase("fire")) {
						e.setDeathMessage(main.getMessages().getSelfBurnMessage(e.getPlayer()));
					}else e.setDeathMessage(main.getMessages().getSuicideMessage(e.getPlayer()));
				}else{
					if(cause.equalsIgnoreCase("lightning")) {
						e.setDeathMessage("§d"+e.getPlayer().getDisplayName()+" was struck down by "+killer.getDisplayName());
					}else if(cause.equalsIgnoreCase("fire")) {
						e.setDeathMessage("§d"+e.getPlayer().getDisplayName()+" was set ablaze by "+killer.getDisplayName());
					}else if(cause.equalsIgnoreCase("wind")) {
						e.setDeathMessage("§d"+killer.getDisplayName()+" has cast "+e.getPlayer().getDisplayName()+" into the abyss!");
					}else if(cause.equalsIgnoreCase("energy") && !voidDmg) {
						e.setDeathMessage("§d"+killer.getDisplayName()+" wouldn't stop poking "+e.getPlayer().getDisplayName());
					}else if(cause.equalsIgnoreCase("energy") && voidDmg) {
						e.setDeathMessage("§d"+e.getPlayer().getDisplayName()+" dove off the edge to escape "+killer.getDisplayName());
					}else{
						e.setDeathMessage("§d"+killer.getDisplayName()+" killed "+e.getPlayer().getDisplayName());
					}
				}
			}
		}else {
			e.setDeathMessage(main.getMessages().getUnusualDeathMessage(e.getPlayer()));
		}
		BukkitRunnable setPots = new BukkitRunnable() {
			@Override
			public void run() {
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
	public void onCombat(PlayerCombatEvent e) {
		Entity attacker = e.getAttacker();
		if(!(attacker instanceof Player)) {
			if(attacker instanceof Projectile) {
				Projectile proj = (Projectile)attacker;
				if(proj.getShooter() != null && proj.getShooter() instanceof Entity) {
					e.setAttacker((Entity)proj.getShooter());
					if(GustSpell.isGust(proj)) {
						e.setCause("wind");
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
	
	//@EventHandler
	public void onWin() {
		
	}
}
