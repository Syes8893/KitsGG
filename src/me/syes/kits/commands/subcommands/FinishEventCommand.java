package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;

public class FinishEventCommand extends SubCommand{

	@Override
	public void execute(Player p, String[] args) {
		if(Kits.getInstance().getEventManager().getActiveEvent() != null) {
			Kits.getInstance().getEventManager().getActiveEvent().finishEvent();
			p.sendMessage("§aEvent successfully finished.");
			return;
		}
		p.sendMessage("§cThere's no event currently running.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /event finish");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
