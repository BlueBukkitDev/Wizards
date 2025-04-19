package dev.blue.wizards;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEvents {
	private Main main;
	/**
	 *Always true if a game event cycle has happened since the last cleanup
	 **/
	private static boolean gameNeedsCleanup = true;
	private int bannerInterval = 140;//seconds
	Random rand;
	public GameEvents(Main main) {
		this.main = main;
		rand = new Random();
	}
	
	public void runGameEvents() {
		//Bukkit.broadcastMessage("§7debug - running events");
		BukkitRunnable runnable = new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				if(main.getGame().gameIsRunning()) {
					if(!gameNeedsCleanup) {
						gameNeedsCleanup = true;
						timer = 0;
						bannerInterval = 140;
					}
					timer++;
					if(main.getGame().getActivePlayers().size() > 1) {
						double d = 140-(main.getGame().getActivePlayers().size()*10);//120 seconds for 2 players, 60 seconds for 8
						bannerInterval = (int) Math.floor(d);
					}
					if(timer == 2) {
						spawnAllBanners();
					}
					if(timer % bannerInterval == bannerInterval-5) {
						for(Player each:Bukkit.getOnlinePlayers()) {
							each.playSound(each.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.4f, 1f);
							each.sendTitle("", "§dSpell banners respawning in 5 seconds...", 2, 20, 12);
						}
					}
					if(timer % bannerInterval == 0) {
						for(Player each:Bukkit.getOnlinePlayers()) {
							each.playSound(each.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0.2f, 1f);
							each.sendTitle("", "§dSpell banners have respawned!", 2, 20, 12);
						}
						bannerInterval += 1;
						spawnAllBanners();
					}
				}else {
					if(main.getGame().gameIsStarting()) {
						cleanup();
					}
				}
			}
		};
		runnable.runTaskTimer(main, bannerInterval, 20);
	}
	
	public void cleanup() {
		//Bukkit.broadcastMessage("§7debug - cleanup");
		if(gameNeedsCleanup) {
			for(Player each:Bukkit.getOnlinePlayers()) {
				main.getUtils().setupInventory(each);
				main.getUtils().resetAllSpells(each);
				main.getUtils().resetPots(each);
				each.teleport(main.getUtils().getCurrentLobby());
			}
			@SuppressWarnings("unchecked")
			List<Location> spellSpawns = (List<Location>) main.getMaps().getList(main.getCurrentMap()+".SpellSpawns");
			for(Location each:spellSpawns) {
				main.getUtils().destroyBanner(each.getBlock());
			}
			gameNeedsCleanup = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void spawnAllBanners() {
		for(Location each:(List<Location>)main.getMaps().getList(main.getCurrentMap()+".SpellSpawns")) {
			if(rand.nextInt(100) < 70) {//70% chance of spawning one at each location. 
				main.getUtils().spawnBanner(each);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private void spawnDrop() {
		for(Location each:(List<Location>)main.getMaps().getList(main.getCurrentMap()+".DropSpawns")) {
			if(rand.nextInt(100) < 70) {//70% chance of spawning one at each location. 
				main.getUtils().spawnBanner(each);
			}
		}
	}
}




