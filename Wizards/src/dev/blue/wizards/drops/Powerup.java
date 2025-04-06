package dev.blue.wizards.drops;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *Increases the level of a single spell by a percentage, but also locks the progression to that level forever. 
 **/
public class Powerup extends Drop {

	public Powerup(Location loc) {
		super("Powerup", loc);
	}

	@Override
	public void spawn() {
		
	}

	@Override
	protected void runCollect(Player p) {
		
	}
}
