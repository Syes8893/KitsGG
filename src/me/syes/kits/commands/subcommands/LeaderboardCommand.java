package me.syes.kits.commands.subcommands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import me.syes.kits.leaderboard.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;

public class LeaderboardCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		if(args[1].equalsIgnoreCase("add")) {
			if(args.length < 3) {
				help(p);
				return;
			}
			if(args[2].equalsIgnoreCase("kills"))
				new KillsLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("exp"))
				new ExpLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("events"))
				new EventLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("killstreak"))
				new KillStreakLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("kdr"))
				new KDRLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("deaths"))
				new DeathsLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("eventsplayed"))
				new EventsPlayedLeaderboard(p.getLocation());
			else if(args[2].equalsIgnoreCase("eventexp"))
				new EventExpLeaderboard(p.getLocation());
			else {
				p.sendMessage("§cInvalid type, valid types are Kills, Exp, Events, Killstreak, KDR, Deaths, Eventsplayed and Eventexp.");
				return;
			}
			p.sendMessage("§aSuccessfully created a new Leaderboard.");
		}else if(args[1].equalsIgnoreCase("remove")) {
			double radius = 3;
			if(args.length > 2)
				radius = Double.parseDouble(args[2]);
			if(radius > 100000)
				radius = 100000;
			for(Entity e : p.getNearbyEntities(radius, radius, radius)) {
				if(e.getType() == EntityType.ARMOR_STAND)
					for(Leaderboard lb : Kits.getInstance().getLeaderboardManager().getLeaderboards()) {
						if(lb.getLocation().distance(p.getLocation()) < radius) lb.removeLeaderboard(true);
					}
			}
			p.sendMessage("§aSuccessfully removed all nearby Leaderboards in a radius of " + new DecimalFormat("#.#").format(radius) + ".");
		}
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /stats leaderboard <add/remove> <type/radius>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}
    
    private HashMap<String, Integer> getBestKillers(final int amount) {
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
    	Comparator<KitPlayer> sorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getKills() > b.getKills()) return 1;
				return -1;
			}
    	};
    	ArrayList<KitPlayer> kitPlayers = new ArrayList<KitPlayer>();
    	for(KitPlayer kp : Kits.getInstance().getPlayerManager().getKitPlayers().values()) {
    		kitPlayers.add(kp);
    	}
    	kitPlayers.sort(sorter);
    	for(KitPlayer kp : kitPlayers) {
    		map.put(Bukkit.getOfflinePlayer(kp.getUuid()).getName(), kp.getKills());
    	}
		return map;
    }

}
