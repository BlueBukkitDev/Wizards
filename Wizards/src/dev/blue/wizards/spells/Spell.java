package dev.blue.wizards.spells;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import dev.blue.wizards.Main;

public abstract class Spell {
	protected static Main main;//Shouldn't ever cause a problem, because by the time a key is referenced, the class will have been properly initialized. 
	protected Player p;
	protected NamespacedKey key;
	protected int cost;
	public Spell(Main mainclass, Player p, NamespacedKey namespacedKey, int cost) {
		main = mainclass;
		this.p = p;
		key = namespacedKey;
		this.cost = cost;
	}
	public abstract void cast();
	public NamespacedKey getKey() {
		return key;
	}
	public int getLevel() {
		return p.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
	}
	public void incrementLevel() {
		p.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, p.getPersistentDataContainer().get(key, PersistentDataType.INTEGER)+1);
	}
	public void resetLevel() {
		p.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 0);
		if(p.getPersistentDataContainer().get(key, PersistentDataType.INTEGER) == null) {
			p.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 0);
		}
	}
	@Override
	public String toString() {
		return key.getNamespace()+":"+key.getKey();
	}
}
