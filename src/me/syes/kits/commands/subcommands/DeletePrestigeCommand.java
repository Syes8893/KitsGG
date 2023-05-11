package me.syes.kits.commands.subcommands;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import org.bukkit.entity.Player;

import java.io.File;

public class DeletePrestigeCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(args[1].equalsIgnoreCase(k.getName() + "_prestige")) {
				Kits.getInstance().getKitManager().removeKit(k);
				File f = new File(Kits.getInstance().getDataFolder() + "/kits/" + k.getName().toString());
				f.delete();
				p.sendMessage("§aSuccessfully deleted the prestige for kit: " + args[1].toString().replace("_prestige", ""));
				return;
			}
		}
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit deleteprestige <name>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
