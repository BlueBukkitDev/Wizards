package dev.blue.wizards.spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import dev.blue.wizards.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LeapSpell extends Spell {

	public LeapSpell(Main main, Player p) {
		super(main, p, new NamespacedKey(main, "wizards-leapSpell"));
	}
	@Override
	public void cast() {
		if(main.getUtils().getRemainingLeaps(p) < 1) {
			return;
		}
		if(p.getFoodLevel() < 10) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		main.getUtils().useLeap(p);
		p.setFoodLevel(p.getFoodLevel()-10);
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6Remaining Leaps: "+main.getUtils().getRemainingLeaps(p)));
		//do things
		if(getLevel() < 10) {
			p.setVelocity(p.getEyeLocation().getDirection().multiply(0.5+(getLevel()/8.5)));
		}else {
			p.setVelocity(p.getEyeLocation().getDirection().multiply(0.7+(getLevel()/8.5)));
		}
		if(!p.getLocation().getBlock().getRelative(BlockFace.DOWN).isPassable()) {
			Location loc = p.getLocation();
			/*loc.setY(Math.floor(p.getLocation().getY()));
			double x = Math.cos(Math.toRadians(p.getLocation().getYaw())) * 0.25d;
			double y = Math.sin(Math.toRadians(p.getLocation().getYaw())) * 0.25d;//This code rotates particles to the player's direction and raises it just above the surface. 
			loc.add(x, 0.025D, y);*/
			loc.getWorld().spawnParticle(Particle.BLOCK_DUST, loc.getX(), loc.getY()+0.1, loc.getZ(), 24, 0.5, 0, 0.5, 0, Material.COARSE_DIRT.createBlockData());
		}
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
	}
}
