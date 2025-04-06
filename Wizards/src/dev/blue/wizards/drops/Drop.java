package dev.blue.wizards.drops;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import dev.blue.wizards.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class Drop {
	private String name;
	private Location loc;
	private String msg;
	
	public Drop(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}
	
	public Drop(String name, Location loc, String msg) {
		this.name = name;
		this.loc = loc;
		this.msg = msg;
	}
	
	/**
	 *Spawns this drop at a predefined location.
	 **/
	public abstract void spawn();
	
	public void collect(Player p) {
		if(msg == null) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§bYou've collected a "+name+"drop!"));
		}else {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
		}
		loc.getWorld().playSound(p, Sound.ENTITY_SHULKER_TELEPORT, 1, 1);
		loc.getWorld().playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
		runCollect(p);
	}
	
	/**
	 *Internal use only; call <code>drop.collect(Player p)</code> instead.
	 **/
	protected abstract void runCollect(Player p);
	
	public void saveToFile() {
		
	}
	
	public String toString() {
		return name+","+loc.getWorld().getUID().toString()+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
	}
	
	public Drop fromString(String s) throws Exception {
		String[] bits = s.split(",");
		if(bits.length != 5) {
			throw new Exception("Incorrect string argument! Requires 5 parameters, separated with commas.");
		}
		if(Bukkit.getWorld(UUID.fromString(bits[1])) == null) {
			throw new Exception("Incorrect string argument! Issue loading drop: invalid world uid.");
		}
		if(!Utils.isNumeric(bits[2])||!Utils.isNumeric(bits[3])||!Utils.isNumeric(bits[4])) {
			throw new Exception("Incorrect number argument(s)! Invalid coordinate values.");
		}
		Location loc = new Location(Bukkit.getWorld(UUID.fromString(bits[1])), Double.parseDouble(bits[2]), Double.parseDouble(bits[3]), Double.parseDouble(bits[4]));
		switch(bits[0]) {
		case "Portal":return new Portal(loc);
		case "Powerup":return new Powerup(loc);
		case "Resistance":return new Resistance(loc);
		case "Speed":return new Speed(loc);
		case "Regen":return new Regen(loc);
		case "Efficiency":return new Efficiency(loc);
		default:throw new Exception("Incorrect string argument! Requires a valid name as the first of 5 values.");
		}
	}
}
