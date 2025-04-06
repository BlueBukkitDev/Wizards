package dev.blue.wizards.drops;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *Teleport a player to another random portal location, destroying both.
 **/
public class Portal extends Drop {

	public Portal(Location loc) {
		super("Name", loc);
	}

	@Override
	public void spawn() {
		
	}

	@Override
	protected void runCollect(Player p) {
		
	}
}
