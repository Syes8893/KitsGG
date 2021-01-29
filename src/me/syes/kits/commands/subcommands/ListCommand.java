package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.ConfigUtils;

public class ListCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		p.sendMessage("§a§lAvailable Kits: §7(" + Kits.getInstance().getKitManager().getKits().size() + ")");
		String str = "";
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			/*String name = k.getName();
			if(ConfigUtils.getConfigSection("Kits").getBoolean("Per-Kit-Permission"))
				if(p.hasPermission("kits." + k.getName().toLowerCase())) name = "§a" + k.getName();
				else name = "§c" + k.getName();*/
			if(ConfigUtils.getConfigSection("Kits").getBoolean("Shortened-Kit-List")) str += "§7, §f" + k.getName();
			else p.sendMessage("§7> §f" + k.getName());
		}
		str = str.replaceFirst("§7, §f", "");
		p.sendMessage(str);
		p.sendMessage("§7");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit list");
	}

	@Override
	public String permission() {
		return "kits.basic";
	}

}
