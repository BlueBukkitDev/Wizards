package dev.blue.wizards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmds implements CommandExecutor {
	private Main main;
	public Cmds(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.isOp()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			List<Location> locations = (List<Location>) main.getConfig().getList("SpellSpawns");
			if(locations == null) {
				locations = new ArrayList<Location>();
			}
			locations.add(p.getLocation().getBlock().getLocation());
			p.sendMessage("§7Added a spell spawn to this location");
			main.getConfig().set("SpellSpawns", locations);
			main.saveConfig();
		}
		return false;
	}
}
