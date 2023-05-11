package me.syes.kits.commands.subcommands.eventcommands;

import me.syes.kits.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;

public class RerollEventCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		Kits.getInstance().getEventManager().rerollNextEvent();
		p.sendMessage("§aSuccessfully rerolled the next Event to " + Kits.getInstance().getEventManager().getNextEvent().getName() + ".");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /event reroll");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
