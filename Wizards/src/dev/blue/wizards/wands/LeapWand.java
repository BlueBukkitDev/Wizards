package dev.blue.wizards.wands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import dev.blue.wizards.Main;

public class LeapWand extends Wand {

	public LeapWand(Main main) {
		super(main, Material.STICK, "wizards-leapSpell");
	}
	
	@Override
	public ItemMeta buildMeta() {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName("§6Leap Wand");
		meta.setLore(Arrays.asList(new String[] {"§7Left-Click to cast a spell"}));
		return meta;
	}
}
