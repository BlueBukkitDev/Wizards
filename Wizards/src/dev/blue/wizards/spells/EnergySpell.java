package dev.blue.wizards.spells;

import java.util.Random;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.blue.wizards.Main;

public class EnergySpell extends Spell {
	
	public EnergySpell(Main main, Player caster) {
		super(main, caster, new NamespacedKey(main, "wizards-energySpell"));
	}
	
	@Override
	public void cast() {
		if(p.getFoodLevel() < 7) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		p.setFoodLevel(p.getFoodLevel()-7);
		if(main.getGame().gameIsRunning()) {
			launchEnergy(p);
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.7f, 0.1f);
		}
	}
	
	private void launchEnergy(Player p) {
		float speed = 3f+(getLevel()/6.5f);
		Vector direction = p.getLocation().getDirection();
		Snowball snowball = p.launchProjectile(Snowball.class, direction.multiply(speed));
		snowball.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
		snowball.setGravity(false);
		snowball.setVisibleByDefault(true);
		snowball.setGlowing(true);
		runParticlesAndSounds(snowball);
	}
	
	private void runParticlesAndSounds(Snowball missile) {
		Random random = new Random();
		BukkitRunnable runnable = new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				timer++;
				if(timer >= 70) {
					missile.remove();
					this.cancel();
				}
				if(missile == null || missile.isDead()) {
					this.cancel();
				}
				if(timer > 2) {
					missile.getWorld().spawnParticle(Particle.GLOW, missile.getLocation(), 10, 0.15, 0.15, 0.15, 0, null);
					missile.getWorld().playSound(missile.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.7f, random.nextFloat(0.1f, 1f));
				}
			}
		};
		runnable.runTaskTimer(main, 0, 1);
	}
}
