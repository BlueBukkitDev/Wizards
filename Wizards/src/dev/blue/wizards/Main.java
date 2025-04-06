package dev.blue.wizards;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

import dev.blue.brawl.BrawlPlugin;
import dev.blue.brawl.modes.BaseGame;
import dev.blue.wizards.listeners.BannerListener;
import dev.blue.wizards.listeners.EditListener;
import dev.blue.wizards.listeners.GameListener;

public class Main extends JavaPlugin {
	private Utils utils;
	private GameEvents gameEvents;
	private BaseGame game;
	private BrawlPlugin brawl;
	private Messages messages;
	public GameMode playMode;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		playMode = GameMode.valueOf(getConfig().getString("GameMode").toUpperCase());
		utils = new Utils(this);
		gameEvents = new GameEvents(this);
		brawl = (BrawlPlugin) Bukkit.getPluginManager().getPlugin("Brawl");
		game = brawl.getGameTimer();
		this.messages = new Messages(this);
		Bukkit.getPluginManager().registerEvents(new BannerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new EditListener(), this);
		Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
		getCommand("addspell").setExecutor(new Cmds(this));
		gameEvents.cleanup();
		gameEvents.runGameEvents();
	}
	
	public Messages getMessages() {
		return messages;
	}
	
	public Utils getUtils() {
		return this.utils;
	}
	
	public GameEvents getSpells() {
		return this.gameEvents;
	}
	
	public BaseGame getGame() {
		return game;
	}
	
	public BrawlPlugin getBrawl() {
		return brawl;
	}
}