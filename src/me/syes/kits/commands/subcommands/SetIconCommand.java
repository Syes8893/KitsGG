package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;

public class SetIconCommand extends SubCommand{

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(k.getName().equalsIgnoreCase(args[1])) {
				k.setIcon(p.getItemInHand().clone());
				p.sendMessage("§aSuccesfully set the icon of " + k.getName() + " to the item in your hand.");
				return;
			}
		}
		p.sendMessage("§cCouldn't find the specified kit.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit seticon <kitname>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
