package es.xdec0de.securitycore.features;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import es.xdec0de.securitycore.SecurityCore;

public class RedstoneLimiter implements Listener {

	HashMap<Location, Integer> executions = new HashMap<>();

	public RedstoneLimiter(SecurityCore plugin) {
		Bukkit.getScheduler().runTaskTimer(plugin, () -> checkExecutions(), 20, 20);
	}

	private void checkExecutions() {
		executions.forEach((loc, exec) -> {
			if (exec > 4)
				loc.getWorld().getBlockAt(loc).breakNaturally();
		});
		executions.clear();
	}

	@EventHandler
	public void onRedstone(BlockRedstoneEvent e) {
		handleRedstonePulse(e.getBlock().getLocation());
	}

	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		handleRedstonePulse(e.getBlock().getLocation());
	}

	@EventHandler
	public void onPistonRetract(BlockPistonExtendEvent e) {
		handleRedstonePulse(e.getBlock().getLocation());
	}

	private void handleRedstonePulse(Location loc) {
		Integer exec = executions.get(loc);
		if (exec == null)
			executions.put(loc, 1);
		else
			executions.put(loc, exec + 1);
	}
}
