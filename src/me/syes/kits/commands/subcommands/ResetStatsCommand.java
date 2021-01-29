package me.syes.kits.commands.subcommands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;

public class ResetStatsCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		if(Bukkit.getOfflinePlayer(args[1]) == null) {
			p.sendMessage("§cSpecified player does not exist.");
			return;
		}
		OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
		File f = new File(Kits.getInstance().getDataFolder() + "/players/" + t.getUniqueId());
		f.delete();
		//Kits.getInstance().playerManager.getKitPlayers().remove(t.getUniqueId());
		if(t.isOnline()) {
			if(Kits.getInstance().getPlayerManager().getKitPlayer(t.getUniqueId()).isInArena()) {
				t.getPlayer().getInventory().clear();
				t.getPlayer().damage(100);
			}
			t.getPlayer().sendMessage("§cYour stats have been reset.");
			new KitPlayer(t.getUniqueId());
		}
			
		p.sendMessage("§a" + t.getName() + "'s stats have been successfully reset.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /stats reset <player>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
