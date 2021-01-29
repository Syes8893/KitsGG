package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;

public class StartEventCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(Kits.getInstance().getEventManager().getActiveEvent() != null) {
			p.sendMessage("§cThere's already an event running!");
			return;
		}
		Kits.getInstance().getEventManager().skipTimeUntilNextEvent();
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /event start");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
