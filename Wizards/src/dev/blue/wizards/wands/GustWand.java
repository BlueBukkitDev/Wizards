package dev.blue.wizards.wands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import dev.blue.wizards.Main;

public class GustWand extends ItemStack {
	private Main main;
	public GustWand(Main main) {
		this.main = main;
	}
	public NamespacedKey getSpellKey() {
		return new NamespacedKey(main, "wizards-gustSpell");
	}
	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(Material.LEVER);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§6Gust Wand");
		meta.getPersistentDataContainer().set(new NamespacedKey(main, "wizards-wand"), PersistentDataType.BYTE, (byte)1);
		meta.setLore(Arrays.asList(new String[] {"§7Right-Click to cast a spell"}));
		stack.setItemMeta(meta);
		return stack;
	}
}
