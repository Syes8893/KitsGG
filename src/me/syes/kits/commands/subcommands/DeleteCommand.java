package me.syes.kits.commands.subcommands;

import java.io.File;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;

public class DeleteCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(args[1].equalsIgnoreCase(k.getName())) {
				Kits.getInstance().getKitManager().removeKit(k);
				File f = new File(Kits.getInstance().getDataFolder() + "/kits/" + k.getName().toString() + ".yml");
				f.delete();
				p.sendMessage("§aSuccessfully deleted the kit: " + args[1].toString());
				return;
			}
		}
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit delete <name>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
