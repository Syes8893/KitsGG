package me.syes.kits.commands.subcommands;

import me.syes.kits.kit.KitManager;
import me.syes.kits.kitplayer.KitPlayer;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.ConfigUtils;

public class ListCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		p.sendMessage("§a§lAvailable Kits: §7(" + Kits.getInstance().getKitManager().getKits().size() + ")");
		String str = "";
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		KitManager km =  Kits.getInstance().getKitManager();
		for(Kit k : km.getKits()) {
			if(k.getName().contains("_prestige"))
				continue;
			if(k.hasPrestige() && kp.getExp() > km.getKit(k.getName() + "_prestige").getRequiredExp())
				k = km.getKit(k.getName() + "_prestige");
			/*String name = k.getName();
			if(ConfigUtils.getConfigSection("Kits").getBoolean("Per-Kit-Permission"))
				if(p.hasPermission("kits." + k.getName().toLowerCase())) name = "§a" + k.getName();
				else name = "§c" + k.getName();*/
			if(ConfigUtils.getConfigSection("Kits").getBoolean("Shortened-Kit-List")) {
				if(ConfigUtils.perKitPermissions && !p.hasPermission("kits." + k.getName()))
					str += "§7, §8" + k.getName() + " (" + k.getRequiredExp() + ")";
				else if(kp.getExp() >= k.getRequiredExp())
					str += "§7, §f" + k.getName() + " (" + k.getRequiredExp() + ")";
				else
					str += "§7, §7" + k.getName() + " (" + k.getRequiredExp() + ")";
			}
			else {
				if(ConfigUtils.perKitPermissions && !p.hasPermission("kits." + k.getName()))
					p.sendMessage("§7> §e" + k.getName());
				else if(kp.getExp() >= k.getRequiredExp())
					p.sendMessage("§7> §f" + k.getName());
				else
					p.sendMessage("§7> §c" + k.getName());
			}
		}
		str = str.replaceFirst("§7, ", "");
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
