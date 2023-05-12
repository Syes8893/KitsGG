package me.syes.kits.commands.subcommands.arenacommands;

import me.syes.kits.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;

public class WarpCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		Kits.getInstance().getArenaManager().warpIntoArena(p);
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /arena warp");
	}

	@Override
	public String permission() {
		return "kits.basic";
	}

}
