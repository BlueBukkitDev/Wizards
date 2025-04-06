package dev.blue.wizards.drops;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *Gives the player a very small movement boost. 
 **/
public class Speed extends Drop {

	public Speed(Location loc) {
		super("Speed", loc);
	}

	@Override
	public void spawn() {
		
	}

	@Override
	protected void runCollect(Player p) {
		
	}
}
