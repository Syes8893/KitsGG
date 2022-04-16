package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.arena.Arena;

public class SetBoundsCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		Arena a = Kits.getInstance().arenaManager.getArena();
		if(args[1].equalsIgnoreCase("min") || args[1].equalsIgnoreCase("1")) {
//			if(a.getMaxBounds() != null &&
//					(a.getMaxBounds().getBlockX() < p.getLocation().getBlockX() || a.getMaxBounds().getBlockZ() < p.getLocation().getBlockZ())) {
//				p.sendMessage("§cThe minimum bounds must be smaller than than the maximum ones, make sure both x and z are negative");
//				return;
//			}
			a.setMinBounds(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			p.sendMessage("§aMinimum bounds successfully updated.");
		}
		else if(args[1].equalsIgnoreCase("max") || args[1].equalsIgnoreCase("2")) {
//			if(a.getMinBounds() != null &&
//					(a.getMinBounds().getBlockX() > p.getLocation().getBlockX() || a.getMinBounds().getBlockZ() > p.getLocation().getBlockZ())) {
//				p.sendMessage("§cThe maximum bounds must be bigger than than the minimum ones, make sure both x and z are positive");
//				return;
//			}
			a.setMaxBounds(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			p.sendMessage("§aMaximum bounds successfully updated.");
		}
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /arena setbounds <min/max>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}
}
