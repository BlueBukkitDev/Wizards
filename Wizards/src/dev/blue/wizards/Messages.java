package dev.blue.wizards;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Messages {
	
	private Random random;
	
	private List<String> selfShockMessages;
	
	private List<String> selfBurnMessages;
	
	private List<String> suicideMessages;
	
	private List<String> unusualDeathMessages;
	
	public Messages(Main main) {
		random = new Random();
		selfShockMessages = main.getConfig().getStringList("Messages.SelfElectrocute");
		selfBurnMessages = main.getConfig().getStringList("Messages.SelfBurn");
		suicideMessages = main.getConfig().getStringList("Messages.Suicide");
		unusualDeathMessages = main.getConfig().getStringList("Messages.Suicide");
	}
	
	public String getSelfShockMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', selfShockMessages.get(random.nextInt(selfShockMessages.size()-1)).replaceAll("$name$", p.getDisplayName()));
	}
	
	public String getSelfBurnMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', selfBurnMessages.get(random.nextInt(selfBurnMessages.size()-1)).replaceAll("$name$", p.getDisplayName()));
	}
	
	public String getSuicideMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', suicideMessages.get(random.nextInt(suicideMessages.size()-1)).replaceAll("$name$", p.getDisplayName()));
	}
	
	public String getUnusualDeathMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', unusualDeathMessages.get(random.nextInt(unusualDeathMessages.size()-1)).replaceAll("$name$", p.getDisplayName()));
	}
}
