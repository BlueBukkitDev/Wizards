package dev.blue.wizards.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.blue.wizards.Main;

public class HealSpell extends Spell {
	
	public HealSpell(Main main, Player caster) {
		super(main, caster, new NamespacedKey(main, "wizards-healSpell"), 8);
	}
	@Override
	public void cast() {
		for(PotionEffect each:p.getActivePotionEffects()) {
			if(each.getType() == PotionEffectType.INVISIBILITY) {
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
		}
		if(p.getHealth() >= p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		if(p.getFoodLevel() < cost) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		p.setFoodLevel(p.getFoodLevel()-cost);
		if(main.getGame().gameIsRunning()) {
			double decMult = 1;
			if(p.getFireTicks() > 1) {
				decMult = 0.5;
			}
			double health = p.getHealth()+((2+(getLevel()*0.5))*decMult);
			if(health > p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				health = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			}
			p.setHealth(health);
			p.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, p.getEyeLocation().getX(), p.getEyeLocation().getY()-0.2, p.getLocation().getZ(), 12, 0.5, 0.2, 0.5, 0, null);
		}
	}
}
//- combat not being reset after death
//- spawn locations are super wonky
//- lightning sparks show even if the cast fails for distance