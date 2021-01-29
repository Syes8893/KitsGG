package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;

public class SetRequiredExpCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 3) {
			help(p);
			return;
		}
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(k.getName().equalsIgnoreCase(args[1])) {
				k.setRequiredExp(Integer.parseInt(args[2]));
				p.sendMessage("§aSuccesfully the Exp requirement for " + k.getName() + " to " + Integer.parseInt(args[2]) + ".");
				return;
			}
		}
		p.sendMessage("§cCouldn't find the specified kit.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit setrequiredexp <kitname> <exp>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
