package me.syes.kits.commands.subcommands.kitcommands;

import me.syes.kits.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;

public class SetIconCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		int level = 1;
		if(args[2] != null)
			level = Integer.parseInt(args[2]);
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(k.getName().equalsIgnoreCase(args[1]) && k.getLevel() == level) {
				k.setIcon(p.getItemInHand().clone());
				p.sendMessage("§aSuccessfully set the icon of " + k.getNameAndLevel() + " to the item in your hand.");
				return;
			}
		}
		p.sendMessage("§cCouldn't find the specified kit.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit seticon <kitname> [level]");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
