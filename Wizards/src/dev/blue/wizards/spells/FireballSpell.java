package dev.blue.wizards.spells;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.blue.wizards.Main;

public class FireballSpell extends Spell {

	public FireballSpell(Main main, Player p) {
		super(main, p, new NamespacedKey(main, "wizards-fireballSpell"));
	}

	@Override
	public void cast() {
		if(p.getFoodLevel() < 12) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		p.setFoodLevel(p.getFoodLevel()-12);
		if(main.getGame().gameIsRunning()) {
			Vector direction = p.getLocation().getDirection();
			Fireball fireball = p.launchProjectile(Fireball.class, direction.normalize());
			fireball.setYield(0);
			fireball.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
			fireball.getWorld().playSound(fireball.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		}
	}
	public void runImpact(Fireball fireball) {
		int level = new FireballSpell(main, (Player)fireball.getShooter()).getLevel();
		fireball.getWorld().playSound(fireball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
		AreaEffectCloud aec = fireball.getWorld().spawn(fireball.getLocation(), AreaEffectCloud.class);
		aec.setColor(Color.PURPLE);
		aec.setDuration(level*20);
		aec.setDurationOnUse(level);
		aec.setParticle(Particle.FLAME);
		aec.setRadius((float)(3+(level/2)+(level/4)));
		aec.setRadiusPerTick(-0.1f);
		aec.setReapplicationDelay(10);
		aec.setSource(fireball.getShooter());
		aec.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, false));
		new BukkitRunnable() {
			@Override
			public void run() {
				aec.remove();
			}
		}.runTaskLater(main, 120);
	}
}
