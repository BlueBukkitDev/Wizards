package dev.blue.wizards.wands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import dev.blue.wizards.Main;

public abstract class Wand extends ItemStack {
	protected Main main;
	private NamespacedKey spellKey;
	
	protected Wand(Main plugin, Material material, String key) {
		this.main = plugin;
		this.spellKey = new NamespacedKey(plugin, key);
		this.setType(material);
		this.setAmount(1);
		ItemMeta meta = buildMeta();
		meta.getPersistentDataContainer().set(new NamespacedKey(main, "wizards-wand"), PersistentDataType.BYTE, (byte)1);
		meta.getPersistentDataContainer().set(spellKey, PersistentDataType.BYTE, (byte)1);
		this.setItemMeta(meta);
	}
	
	protected NamespacedKey getKey() {
		return this.spellKey;
	}
	
	public abstract ItemMeta buildMeta();
}