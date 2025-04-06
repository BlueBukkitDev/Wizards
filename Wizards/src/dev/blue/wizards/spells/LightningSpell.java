package dev.blue.wizards.spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.blue.wizards.Main;

public class LightningSpell extends Spell {

	public LightningSpell(Main main, Player p) {
		super(main, p, new NamespacedKey(main, "wizards-lightningSpell"), 18);
	}

	@Override
	public void cast() {
		for(PotionEffect each:p.getActivePotionEffects()) {
			if(each.getType() == PotionEffectType.INVISIBILITY) {
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
		}
		if(p.getFoodLevel() < cost) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		Location loc = p.getEyeLocation();
		Vector dir = loc.getDirection();
		int range = 20+(8*getLevel());
		Block b = null;
	    for (int i = 0; i <= range; i++) {
	        b = loc.add(dir).getBlock();
	        if(!b.isPassable()) {
	        	break;
	        }
	    }
	    final Location strikeLoc = b.getWorld().getHighestBlockAt(b.getLocation()).getLocation().add(0, 1, 0);
	    p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, strikeLoc.getX(), strikeLoc.getY(), strikeLoc.getZ(), 15, 0.5, 4.0, 0.5, 0, null);
	    p.spawnParticle(Particle.ELECTRIC_SPARK, strikeLoc.getX(), strikeLoc.getY(), strikeLoc.getZ(), 90, 0.5, 4.0, 0.5, 0, null);
	    if(b.getType() != Material.AIR) {
	    	p.setFoodLevel(p.getFoodLevel()-cost);
	    	p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
	    	BukkitRunnable strikeRunnable = new BukkitRunnable() {
	    		@Override
	    		public void run() {
	    			if(main.getGame().gameIsRunning()) {
	    				LightningStrike strike = p.getWorld().strikeLightning(strikeLoc);
	    				strike.setCustomNameVisible(false);
	    				strike.setCustomName(p.getUniqueId().toString());
	    			}
	    		}
	    	};
	    	strikeRunnable.runTaskLater(main, (20*6) - (getLevel()*10));
	    }
	}
}
