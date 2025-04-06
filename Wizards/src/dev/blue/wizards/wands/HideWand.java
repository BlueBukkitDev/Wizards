package dev.blue.wizards.wands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import dev.blue.wizards.Main;

public class HideWand extends Wand {
	
	public HideWand(Main main) {
		super(main, Material.GHAST_TEAR, "wizards-hideSpell");
	}
	
	@Override
	public ItemMeta buildMeta() {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName("§6Hide Wand");
		meta.setLore(Arrays.asList(new String[] {"§7Left-Click to cast a spell"}));
		return meta;
	}

}
