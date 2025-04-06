package dev.blue.wizards.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.blue.wizards.Main;

public class LevitateSpell extends Spell {
	public LevitateSpell(Main main, Player caster) {
		super(main, caster, new NamespacedKey(main, "wizards-levitateSpell"), 8);
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
			launchShulkerTooth(p);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 0.7f, 0.1f);
		}
	}
	
	private void launchShulkerTooth(Player p) {
		float speed = 0.5f+(getLevel()/10f);
		Vector direction = p.getLocation().getDirection();
		ShulkerBullet tooth = p.launchProjectile(ShulkerBullet.class, direction.multiply(speed));
		tooth.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
		tooth.setGravity(false);
		tooth.setVisibleByDefault(true);
		maintainExistence(tooth);
	}
	
	private void maintainExistence(ShulkerBullet tooth) {
		BukkitRunnable runnable = new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				timer++;
				if(timer >= 40) {
					tooth.remove();
					this.cancel();
				}
				if(tooth == null || tooth.isDead()) {
					this.cancel();
				}
			}
		};
		runnable.runTaskTimer(main, 0, 1);
	}
	
	public static boolean isLevitate(Projectile p) {
		return p.getPersistentDataContainer().has(new NamespacedKey(main, "wizards-levitateSpell"), PersistentDataType.BYTE);
	}
}
