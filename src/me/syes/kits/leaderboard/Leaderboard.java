package me.syes.kits.leaderboard;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;

public abstract class Leaderboard {
	
	protected HashMap<ArmorStand, Integer> entities;
	protected ArmorStand title;
	protected Location loc;
	protected int amt;
	protected LeaderboardType lbType;
	
	protected boolean deleted;
	
	public enum LeaderboardType {
		KILLS, EXP, EVENTS, KILLSTREAK, KDR, DEATHS, EVENTSPLAYED;
	}
	
	public Leaderboard(Location loc, LeaderboardType lbType) {
		entities = new HashMap<ArmorStand, Integer>();
		this.loc = loc;
		this.lbType = lbType;
		this.createLeaderboard(loc);
		Kits.getInstance().getLeaderboardManager().addLeaderboard(this);
	}
	
	public abstract void createLeaderboard(Location loc);
	
	public void removeLeaderboard(boolean permanently) {
		for(ArmorStand as : entities.keySet()) as.remove();
		title.remove();
		if(permanently) deleted = true;
	}
	
	public void recreateLeaderboard() {
		removeLeaderboard(false);
		createLeaderboard(loc);
	}
	
	public void updateLeaderboard(HashMap<KitPlayer, Integer> playerScores) {
		for(ArmorStand as : entities.keySet()) {
			for(KitPlayer kp : playerScores.keySet()) {
				if(playerScores.get(kp) < 11)
					if(playerScores.get(kp) == entities.get(as)) {
						setName(as, kp);
						break;
					}
			}
		}
		if(playerScores.keySet().size() > amt && amt < 10) {
			recreateLeaderboard();
		}
	}
	
	public abstract void setName(ArmorStand as, KitPlayer kp);
	
	protected void createArmorStandLine(Integer position, KitPlayer kp, Location loc) {
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, (amt - position) * 0.35 - 2, 0), EntityType.ARMOR_STAND);
		entities.put(as, position);
		as.setGravity(false);
		as.setVisible(false);
		as.setCustomNameVisible(true);
		setName(as, kp);
		//as.setCustomName("§7#" + position + " §f" + kp.getName() + ": §a" + kp.getKills() + " kills");
	}
	
	protected void createArmorStandTitle(String name, Location loc) {
		title = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, (amt) * 0.35 - 2, 0), EntityType.ARMOR_STAND);
		title.setGravity(false);
		title.setVisible(false);
		title.setCustomNameVisible(true);
		title.setCustomName(name.replace("&", "§"));
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public LeaderboardType getLeaderboardType() {
		return this.lbType;
	}
	
	public boolean hasBeenDeleted() {
		return deleted;
	}

}
