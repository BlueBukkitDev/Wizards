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
	
	private List<String> lightningMessages;
	private List<String> fireMessages;
	private List<String> energyMessages;
	private List<String> gustMessages;
	private List<String> vLightningMessages;
	private List<String> vFireMessages;
	private List<String> vEnergyMessages;
	private List<String> vGustMessages;

	public Messages(Main main) {
		random = new Random();
		selfShockMessages = main.getConfig().getStringList("Messages.SelfElectrocute");
		selfBurnMessages = main.getConfig().getStringList("Messages.SelfBurn");
		suicideMessages = main.getConfig().getStringList("Messages.Suicide");
		unusualDeathMessages = main.getConfig().getStringList("Messages.Unknown");
		
		lightningMessages = main.getConfig().getStringList("Messages.KilledByLightning");
		fireMessages = main.getConfig().getStringList("Messages.KilledByFire");
		energyMessages = main.getConfig().getStringList("Messages.KilledByEnergy");
		gustMessages = main.getConfig().getStringList("Messages.KilledByGust");
		vLightningMessages = main.getConfig().getStringList("Messages.KilledByVoid.AfterLightning");
		vFireMessages = main.getConfig().getStringList("Messages.KilledByVoid.AfterFire");
		vEnergyMessages = main.getConfig().getStringList("Messages.KilledByVoid.AfterEnergy");
		vGustMessages = main.getConfig().getStringList("Messages.KilledByVoid.AfterGust");
	}
	
	public String getSelfShockMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', selfShockMessages.get(random.nextInt(selfShockMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getSelfBurnMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', selfBurnMessages.get(random.nextInt(selfBurnMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getSuicideMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', suicideMessages.get(random.nextInt(suicideMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getUnusualDeathMessage(String name) {
		return ChatColor.translateAlternateColorCodes('&', unusualDeathMessages.get(random.nextInt(unusualDeathMessages.size()-1)).replaceAll("_name_", name));
	}
	
	
	
	public String getLightningMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', lightningMessages.get(random.nextInt(lightningMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getFireMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', fireMessages.get(random.nextInt(fireMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getEnergyMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', energyMessages.get(random.nextInt(energyMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getGustMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', gustMessages.get(random.nextInt(gustMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getVLightningMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', vLightningMessages.get(random.nextInt(vLightningMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getvFireMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', vFireMessages.get(random.nextInt(vFireMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getVEnergyMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', vEnergyMessages.get(random.nextInt(vEnergyMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
	
	public String getvGustMessage(Player p) {
		return ChatColor.translateAlternateColorCodes('&', vGustMessages.get(random.nextInt(vGustMessages.size()-1)).replaceAll("_name_", p.getDisplayName()));
	}
}
