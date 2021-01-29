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
		if(args[1].equalsIgnoreCase("min")) {
			if(a.getMaxBounds() != null &&
					(a.getMaxBounds().getBlockX() < p.getLocation().getBlockX() || a.getMaxBounds().getBlockZ() < p.getLocation().getBlockZ())) {
				p.sendMessage("§cThe minimum bounds MUST be smaller than than the maximum ones.");
				return;
			}
			a.setMinBounds(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
			p.sendMessage("§aMinimum bounds succesfully updated.");
		}
		else if(args[1].equalsIgnoreCase("max")) {
			if(a.getMinBounds() != null &&
					(a.getMinBounds().getBlockX() > p.getLocation().getBlockX() || a.getMinBounds().getBlockZ() > p.getLocation().getBlockZ())) {
				p.sendMessage("§cThe maximum bounds MUST be bigger than than the minimum ones.");
				return;
			}
			a.setMaxBounds(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
			p.sendMessage("§aMaximum bounds succesfully updated.");
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
