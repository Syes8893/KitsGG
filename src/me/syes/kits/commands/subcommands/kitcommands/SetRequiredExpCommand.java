package me.syes.kits.commands.subcommands.kitcommands;

import me.syes.kits.commands.subcommands.SubCommand;
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
		int level = 1;
		if(args[3] != null)
			level = Integer.parseInt(args[3]);
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(k.getName().equalsIgnoreCase(args[1]) && k.getLevel() == level) {
				k.setRequiredExp(Integer.parseInt(args[2]));
				p.sendMessage("§aSuccessfully set the Exp requirement for " + k.getNameAndLevel() + " to " + Integer.parseInt(args[2]) + ".");
				return;
			}
		}
		p.sendMessage("§cCouldn't find the specified kit.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit setrequiredexp <kitname> <exp> [level]");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
