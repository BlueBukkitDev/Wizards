package dev.blue.wizards.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class EditListener implements Listener {
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
}
