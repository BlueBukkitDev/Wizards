package dev.blue.wizards;

import org.bukkit.entity.Player;

public class MapSession {
	private Player p;
	private String map;
	
	public MapSession(Player p, String map) {
		this.p = p;
		this.map = map;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public String getMap() {
		return map;
	}
	
	public void setMap(String map) {
		this.map = map;
	}
}
