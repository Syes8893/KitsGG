package me.syes.kits.commands.subcommands.arenacommands;

import me.syes.kits.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;

public class SetWorldCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		Kits.getInstance().arenaManager.getArena().setLobbySpawn(p.getLocation());
		p.sendMessage("§aSuccesfully updated the arena world.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /arena setworld");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
