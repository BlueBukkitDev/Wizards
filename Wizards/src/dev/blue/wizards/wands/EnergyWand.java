package dev.blue.wizards.wands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import dev.blue.wizards.Main;

public class EnergyWand extends Wand {

	public EnergyWand(Main main) {
		super(main, Material.SOUL_TORCH, "wizards-energySpell");
	}
	
	@Override
	public ItemMeta buildMeta() {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName("§6Energy Wand");
		meta.setLore(Arrays.asList(new String[] {"§7Left-Click to cast a spell"}));
		return meta;
	}
}
