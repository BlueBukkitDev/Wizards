package dev.blue.wizards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmds implements CommandExecutor {
	private Main main;
	private List<MapSession> sessions;
	private String err = "§7[Wizards] §cOof! §7";
	private String yee = "§7[Wizards] §6";
	
	public Cmds(Main main) {
		this.main = main;
		sessions = new ArrayList<MapSession>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(yee+"§7Version: "+main.getDescription().getVersion());
			return true;
		}
		if(!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;
		if(!p.isOp() && !p.hasPermission("wizards.admin")) {
			return false;
		}

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("createmap")) {
				p.sendMessage(err+"You must specify a name for your new map!");
				return true;
			}else if(args[0].equalsIgnoreCase("pickmap")) {
				return direct1Args(p, null, args);
			}
			for(MapSession each:sessions) {
				if(each.getPlayer() == p) {
					return direct1Args(p, each, args);
				}
			}
			p.sendMessage(err+"You are not in a Map Editing Session! Use §6/wizards edit <map> §7to begin.");
			return true;
		}
		if(args.length == 2) {
			return direct2Args(p, args);
		}
		return false;
	}
	
	private boolean direct1Args(Player p, MapSession session, String[] args) {
		if(args[0].equalsIgnoreCase("addspell")) {
			if(p.getLocation().getY()%1 > 0) {
				p.sendMessage(err+"You cannot spawn spells on half-blocks!");
				return true;
			}
			@SuppressWarnings("unchecked")
			List<Location> locations = (List<Location>) main.getMaps().getList(session.getMap()+".SpellSpawns");
			if(locations == null) {
				locations = new ArrayList<Location>();
			}
			locations.add(p.getLocation().getBlock().getLocation());
			p.sendMessage(yee+"Added a spell spawn to this location for map "+session.getMap());
			main.getMaps().set(session.getMap()+".SpellSpawns", locations);
			main.saveMaps();
			return true;
		}else if(args[0].equalsIgnoreCase("adddrop")) {
			if(p.getLocation().getY()%1 > 0) {
				p.sendMessage(err+"You cannot spawn drops on half-blocks!");
				return true;
			}
			@SuppressWarnings("unchecked")
			List<Location> locations = (List<Location>) main.getMaps().getList(session.getMap()+".DropSpawns");
			if(locations == null) {
				locations = new ArrayList<Location>();
			}
			locations.add(p.getLocation().getBlock().getLocation());
			p.sendMessage(yee+"Added a drop spawn to this location for map "+session.getMap());
			main.getMaps().set(session.getMap()+".DropSpawns", locations);
			main.saveMaps();
			return true;
		}else if(args[0].equalsIgnoreCase("addspawn")) {
			if(p.getLocation().getY()%1 > 0) {
				p.sendMessage(err+"You cannot spawn players on half-blocks!");
				return true;
			}
			@SuppressWarnings("unchecked") 
			List<Location> locations = (List<Location>) main.getMaps().getList(session.getMap()+".PlayerSpawns");
			if(locations == null) {
				locations = new ArrayList<Location>();
			}
			locations.add(p.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5));
			p.sendMessage(yee+"Added a player spawn to this location for map "+session.getMap());
			main.getMaps().set(session.getMap()+".PlayerSpawns", locations);
			main.saveMaps();
			return true;
		}else if(args[0].equalsIgnoreCase("setlobby")) {
			if(p.getLocation().getY()%1 > 0) {
				p.sendMessage(err+"You cannot spawn players on half-blocks!");
				return true;
			}
			p.sendMessage(yee+"Added a lobby location for map "+session.getMap());
			main.getMaps().set(session.getMap()+".Lobby", p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
			main.saveMaps();
			return true;
		}else if(args[0].equalsIgnoreCase("pickmap")) {
			if(main.getGame().gameIsRunning()) {
				p.sendMessage(err+"You cannot run this command while a game is in progress!");
				return true;
			}
			main.getUtils().selectRandomMap();
			for(Player each:Bukkit.getOnlinePlayers()) {
				each.teleport(main.getUtils().getCurrentLobby());
			}
			p.sendMessage(yee+"You have opted for a different map!");
			return true;
		}
		return false;
	}
	
	private boolean direct2Args(Player p, String[] args) {
		if(args[0].equalsIgnoreCase("edit")) {
			if(!main.getMaps().contains(args[1].toLowerCase())) {
				p.sendMessage(err+"That map doesn't seem to exist.");
				return true;
			}
			for(MapSession each:sessions) {
				if(each.getPlayer() == p) {
					each.setMap(args[1].toLowerCase());
					p.sendMessage(yee+"You are now editing map "+args[1].toLowerCase());
					return true;
				}
			}
			sessions.add(new MapSession(p, args[1].toLowerCase()));
			p.sendMessage(yee+"You are now editing map "+args[1].toLowerCase());
			return true;
		}else if(args[0].equalsIgnoreCase("createmap")) {
			if(main.getMaps().getKeys(false).contains(args[1].toLowerCase())) {
				p.sendMessage(err+"A map already exists by that name! Try again with a different name.");
				return true;
			}
			main.getMaps().createSection(args[1].toLowerCase());
			main.saveMaps();
			p.sendMessage(yee+"You have created a new map, \""+args[1].toLowerCase()+"\"");
			return true;
		}else {
			p.sendMessage(err+"Incorrect args! Use §6/wizards edit <map> §7to begin making changes.");
			return true;
		}
	}
}
