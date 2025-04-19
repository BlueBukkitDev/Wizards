package dev.blue.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class Tabs implements TabCompleter {
	private Main main;

	public Tabs(Main main) {
		this.main = main;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> options = new ArrayList<String>(), results = new ArrayList<String>();
		if (args.length == 1) {
			options = Arrays.asList("createmap", "edit", "addspawn", "addspell", "adddrop", "setlobby", "pickmap");
		}else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("edit")) {/////////////// Does not list the options
				options = new ArrayList<String>(main.getMaps().getKeys(false));
			}else if (args[0].equalsIgnoreCase("createmap")) {
				options = Arrays.asList("untitled");
			}
		}
		StringUtil.copyPartialMatches(args[0], options, results);
		Collections.sort(results);
		return results;
	}
}
