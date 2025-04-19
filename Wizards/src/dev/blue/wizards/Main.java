package dev.blue.wizards;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.blue.brawl.BrawlPlugin;
import dev.blue.brawl.modes.BaseGame;
import dev.blue.wizards.listeners.BannerListener;
import dev.blue.wizards.listeners.EditListener;
import dev.blue.wizards.listeners.GameListener;

public class Main extends JavaPlugin {
	private Utils utils;
	private File mapsFile;
	private YamlConfiguration mapsYaml;
	private GameEvents gameEvents;
	private BaseGame game;
	private BrawlPlugin brawl;
	private Messages messages;
	private String currentMap;
	public GameMode playMode;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		setupYamls();
		playMode = GameMode.valueOf(getConfig().getString("GameMode").toUpperCase());
		utils = new Utils(this);
		gameEvents = new GameEvents(this);
		brawl = (BrawlPlugin) Bukkit.getPluginManager().getPlugin("Brawl");
		WizardsGame customGame = new WizardsGame(brawl, this);
		brawl.setGameTimer(customGame);
		game = customGame;
		utils.selectRandomMap();
		game.setLobbySpawnLocation(utils.getCurrentLobby());
		this.messages = new Messages(this);
		Bukkit.getPluginManager().registerEvents(new BannerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new EditListener(), this);
		Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
		Cmds cmds = new Cmds(this);
		Tabs tabs = new Tabs(this);
		getCommand("wizards").setExecutor(cmds);
		getCommand("wizards").setTabCompleter(tabs);
		gameEvents.cleanup();
		gameEvents.runGameEvents();
	}
	
	public String getCurrentMap() {
		return currentMap;
	}
	
	public void setCurrentMap(String s) {
		this.currentMap = s;
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
	
	public void forceCleanup() {
		gameEvents.cleanup();
	}
	
	public BaseGame getGame() {
		return game;
	}
	
	public BrawlPlugin getBrawl() {
		return brawl;
	}
	
	public YamlConfiguration getMaps() {
		return this.mapsYaml;
	}

	public void saveMaps() {
		try {
			this.mapsYaml.save(this.mapsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setupYamls() {
		this.mapsFile = new File(getDataFolder(), "maps.yml");
		if (!this.mapsFile.exists())
			saveResource("maps.yml", false);
		this.mapsYaml = new YamlConfiguration();
		try {
			this.mapsYaml.load(this.mapsFile);
		} catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}