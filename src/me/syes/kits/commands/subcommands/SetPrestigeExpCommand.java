package me.syes.kits.commands.subcommands;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import org.bukkit.entity.Player;

public class SetPrestigeExpCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 3) {
			help(p);
			return;
		}
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(k.getName().equalsIgnoreCase(args[1] + "_prestige")) {
				k.setRequiredExp(Integer.parseInt(args[2]));
				p.sendMessage("§aSuccessfully set the prestige Exp requirement for " + args[1] + " to " + Integer.parseInt(args[2]) + ".");
				return;
			}
		}
		p.sendMessage("§cCouldn't find the specified kit.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit setprestigexp <kitname> <exp>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
