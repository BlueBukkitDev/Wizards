package dev.blue.wizards;

import java.util.UUID;

import org.bukkit.Bukkit;

import dev.blue.brawl.BrawlPlugin;
import dev.blue.brawl.modes.BodyCount;

public class WizardsGame extends BodyCount {
	private Main main;

	public WizardsGame(BrawlPlugin brawl, Main main) {
		super(brawl);
		this.main = main;
	}
	
	@Override
	public void onGameStart() {
		for(String each:contestants.keySet()) {
			Bukkit.getPlayer(UUID.fromString(each)).teleport(main.getUtils().getRandomMapSpawn());
		}
	}
}
