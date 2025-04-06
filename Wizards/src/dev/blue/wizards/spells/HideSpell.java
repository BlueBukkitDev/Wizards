package dev.blue.wizards.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.blue.wizards.Main;

public class HideSpell extends Spell {
	public HideSpell(Main main, Player caster) {
		super(main, caster, new NamespacedKey(main, "wizards-hideSpell"), 18);
	}
	@Override
	public void cast() {
		if(p.getFoodLevel() < cost) {
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
			return;
		}
		p.setFoodLevel(p.getFoodLevel()-cost);
		if(main.getGame().gameIsRunning()) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20+(getLevel()*10), 1, false, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20+(getLevel()*10), 1, false, false, false));
			p.getWorld().spawnParticle(Particle.SMOKE, p.getEyeLocation().getX(), p.getEyeLocation().getY()-0.2, p.getLocation().getZ(), 350, 0.5, 0.5, 0.5, 0, null);
		}
	}
}
