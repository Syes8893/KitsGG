package me.syes.kits.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;

public class SetStatsCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 4) {
			help(p);
			return;
		}
		if(Bukkit.getOfflinePlayer(args[1]) == null) {
			p.sendMessage("§cCould not find the specified player.");
			return;
		}
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(target.getUniqueId());
		int amt = Integer.parseInt(args[3]);
		if(args[2].equalsIgnoreCase("kills"))
			kp.setKills(amt);
		else if(args[2].equalsIgnoreCase("deaths"))
				kp.setDeaths(amt);
		else if(args[2].equalsIgnoreCase("eventsplayed"))
			kp.setEventsPlayed(amt);
		else if(args[2].equalsIgnoreCase("eventswon"))
			kp.setEventsWon(amt);
		else {
			p.sendMessage("§cInvalid type, valid types are Kills, Deaths, Eventsplayed and Eventswon.");
			return;
		}
		p.sendMessage("§aSuccesfully updated " + kp.getName() + "'s Stats!");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /stats set <player> <kills/deaths/eventplayed/eventswon> <amount>");
	}

	@Override
	public String permission() {
		return "kits.basic";
	}

}
