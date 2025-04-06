package dev.blue.wizards.wands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import dev.blue.wizards.Main;

public class FireballWand extends Wand {
	public FireballWand(Main main) {
		super(main, Material.REDSTONE_TORCH, "wizards-fireballSpell");
	}
	
	@Override
	public ItemMeta buildMeta() {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName("§6Fireball Wand");
		meta.setLore(Arrays.asList(new String[] {"§7Left-Click to cast a spell"}));
		return meta;
	}
}
