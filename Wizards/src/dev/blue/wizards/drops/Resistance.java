package dev.blue.wizards.drops;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *Gives the player a very small resistance. 
 **/
public class Resistance extends Drop {

	public Resistance(Location loc) {
		super("Resistance", loc);
	}

	@Override
	public void spawn() {
		
	}

	@Override
	protected void runCollect(Player p) {
		
	}
}
