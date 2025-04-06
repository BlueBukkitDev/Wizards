package dev.blue.wizards.drops;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *Gives the player a very small regen.
 **/
public class Regen extends Drop {

	public Regen(Location loc) {
		super("Regen", loc);
	}

	@Override
	public void spawn() {
		
	}

	@Override
	protected void runCollect(Player p) {
		
	}
}
