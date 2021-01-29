package me.syes.kits.leaderboard;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;

public class KillStreakLeaderboard extends Leaderboard{
	
	public KillStreakLeaderboard(Location loc) {
		super(loc, LeaderboardType.KILLSTREAK);
	}
	
	public void createLeaderboard(Location loc) {
		amt = 10;
		if(Kits.getInstance().getPlayerManager().getKitPlayers().size() < 10) amt = Kits.getInstance().getPlayerManager().getKitPlayers().size();
		HashMap<KitPlayer, Integer> map = Kits.getInstance().getLeaderboardManager().getTopPlayers(amt, lbType);
		for(KitPlayer kp : map.keySet()) {
			if(map.get(kp) < 11)
			createArmorStandLine(map.get(kp), kp, loc);
		}
		createArmorStandTitle("&a&lTop Killstreaks", loc);
	}
	
	public void setName(ArmorStand as, KitPlayer kp) {
		as.setCustomName("§7#" + entities.get(as) + " " + Kits.getInstance().getExpManager().getLevel(kp.getExp()).getPrefix() + kp.getName() + ": §a" + kp.getHighestKillstreak() + " Kills");
	}
	
}
