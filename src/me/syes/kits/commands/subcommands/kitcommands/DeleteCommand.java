package me.syes.kits.commands.subcommands.kitcommands;

import java.io.File;

import me.syes.kits.commands.subcommands.SubCommand;
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
		int level = 1;
		if(args.length > 2)
			level = Integer.parseInt(args[2]);
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(args[1].equalsIgnoreCase(k.getName()) && k.getLevel() == level) {
				if(k.getLevel() > 1)
					Kits.getInstance().getKitManager().getKit(args[1], level-1).setHasUpgrade(false);
				Kits.getInstance().getKitManager().removeKit(k);
				File f = new File(Kits.getInstance().getDataFolder() + "/kits/" + k.getFileName().toLowerCase() + ".yml");
				System.out.println(k.getFileName().toLowerCase());
				f.delete();
				p.sendMessage("§aSuccessfully deleted the kit: " + k.getNameAndLevel() + ".");
				return;
			}
		}
		p.sendMessage("Couldn't find the kit: " + args[1] + " (Level" + level + ").");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit delete <name> [level]");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
