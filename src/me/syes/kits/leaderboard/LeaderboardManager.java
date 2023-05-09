package me.syes.kits.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.leaderboard.Leaderboard.LeaderboardType;

public class LeaderboardManager {
	
	private HashSet<Leaderboard> leaderboards;
	
	public LeaderboardManager() {
		this.leaderboards = new HashSet<Leaderboard>();
		this.updateLeaderboards();
	}
	
	public void updateLeaderboards() {
		new BukkitRunnable() {
			public void run() {
				for(Leaderboard lb : leaderboards) {
					lb.updateLeaderboard(getTopPlayers(10, lb.getLeaderboardType()));
				}
			}
		}.runTaskTimer(Kits.getInstance(), 240, 240);
	}

	public void removeLeaderboards(boolean shutdown) {
		for(Leaderboard lb : this.getLeaderboards()){
			lb.removeLeaderboard(false);
		}
		if(shutdown)
			this.leaderboards.clear();
	}
    
    public HashMap<KitPlayer, Integer> getTopPlayers(int amount, LeaderboardType lbType) {
    	//Create sorters
    	Comparator<KitPlayer> killSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getKills() > b.getKills()) return -1;
				else if(a.getKills() < b.getKills()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
    	};
    	Comparator<KitPlayer> expSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
    	};
    	Comparator<KitPlayer> eventSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getEventsWon() > b.getEventsWon()) return -1;
				else if(a.getEventsWon() < b.getEventsWon()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
    	};
    	Comparator<KitPlayer> killstreakSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getHighestKillstreak() > b.getHighestKillstreak()) return -1;
				else if(a.getHighestKillstreak() < b.getHighestKillstreak()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
    	};
    	Comparator<KitPlayer> kdrSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getKDR() > b.getKDR()) return -1;
				else if(a.getKDR() < b.getKDR()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
    	};
		Comparator<KitPlayer> deathsSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getDeaths() > b.getDeaths()) return -1;
				else if(a.getDeaths() < b.getDeaths()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
		};
		Comparator<KitPlayer> eventsPlayedSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getEventsPlayed() > b.getEventsPlayed()) return -1;
				else if(a.getEventsPlayed() < b.getEventsPlayed()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
		};
		Comparator<KitPlayer> eventExpSorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(a.getEventsPlayed() > b.getEventsPlayed()) return -1;
				else if(a.getEventsPlayed() < b.getEventsPlayed()) return 1;
				if(a.getExp() > b.getExp()) return -1;
				else if(a.getExp() < b.getExp()) return 1;
				return 0;
			}
		};
    	
    	HashMap<KitPlayer, Integer> map = new HashMap<KitPlayer, Integer>();
    	ArrayList<KitPlayer> kitPlayers = new ArrayList<KitPlayer>();
    	for(KitPlayer kp : Kits.getInstance().getPlayerManager().getKitPlayers().values()) {
    		kitPlayers.add(kp);
    	}
    	if(lbType == null)
    		kitPlayers.sort(killSorter);
    	else if(lbType.equals(LeaderboardType.KILLS))
    		kitPlayers.sort(killSorter);
    	else if(lbType.equals(LeaderboardType.EXP))
    		kitPlayers.sort(expSorter);
    	else if(lbType.equals(LeaderboardType.EVENTS))
    		kitPlayers.sort(eventSorter);
    	else if(lbType.equals(LeaderboardType.KILLSTREAK))
    		kitPlayers.sort(killstreakSorter);
    	else if(lbType.equals(LeaderboardType.KDR))
    		kitPlayers.sort(kdrSorter);
		else if(lbType.equals(LeaderboardType.DEATHS))
			kitPlayers.sort(deathsSorter);
		else if(lbType.equals(LeaderboardType.EVENTSPLAYED))
			kitPlayers.sort(eventsPlayedSorter);
		else if(lbType.equals(LeaderboardType.EVENTEXP))
			kitPlayers.sort(eventExpSorter);
    	for(KitPlayer kp : kitPlayers) {
    		map.put(kp, kitPlayers.indexOf(kp)+1);
    	}
		return map;
    }
    
    public HashSet<Leaderboard> getLeaderboards() {
    	return this.leaderboards;
    }
    
    public void addLeaderboard(Leaderboard lb) {
    	this.leaderboards.add(lb);
    }
    
    public void removeLeaderboard(Leaderboard lb) {
    	this.leaderboards.remove(lb);
    }

}
