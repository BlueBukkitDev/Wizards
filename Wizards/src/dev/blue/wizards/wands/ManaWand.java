package dev.blue.wizards.wands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import dev.blue.wizards.Main;

public class ManaWand extends ItemStack {
	private Main main;
	public ManaWand(Main main) {
		this.main = main;
		this.setType(Material.BLAZE_ROD);
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName("§6Mana Wand");
		meta.getPersistentDataContainer().set(new NamespacedKey(main, "wizards-wand"), PersistentDataType.BYTE, (byte)1);
		meta.setLore(Arrays.asList(new String[] {"§7Right-Click to cast a spell"}));
		this.setItemMeta(meta);
	}
	public NamespacedKey getSpellKey() {
		return new NamespacedKey(main, "wizards-manaSpell");
	}
}
