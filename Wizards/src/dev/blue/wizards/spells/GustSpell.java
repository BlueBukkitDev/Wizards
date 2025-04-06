package dev.blue.wizards.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.blue.wizards.Main;

public class GustSpell extends Spell {
	
	public GustSpell(Main main, Player caster) {
		super(main, caster, new NamespacedKey(main, "wizards-gustSpell"), 8);
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
		p.setFoodLevel(p.getFoodLevel()-cost);
		if(main.getGame().gameIsRunning()) {
			int instances = 15;
			int index = 0;
			while(index < instances) {
				launchGust(p);
				index++;
			}
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		}
	}
	
	private void launchGust(Player p) {
		float accuracy = 0.22f;
		Vector direction = p.getLocation().getDirection();
		Snowball snowball = p.launchProjectile(Snowball.class, main.getUtils().randomizeVector(direction, accuracy).multiply((getLevel() * 0.1)+1));
		snowball.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
		snowball.setGravity(false);
		snowball.setVisibleByDefault(false);
		runGustParticles(snowball);
	}
	
	private void runGustParticles(Snowball missile) {
		BukkitRunnable runnable = new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				timer++;
				if(timer >= 10) {
					missile.remove();
				}
				if(missile == null || missile.isDead()) {
					this.cancel();
				}
				if(timer >= 4) {
					missile.getWorld().spawnParticle(Particle.GUST, missile.getLocation(), 1, 0.3, 0.3, 0.3, 0, null);
				}
			}
		};
		runnable.runTaskTimer(main, 0, 1);
	}
	
	public static boolean isGust(Projectile p) {
		return p.getPersistentDataContainer().has(new NamespacedKey(main, "wizards-gustSpell"), PersistentDataType.BYTE);
	}
}
