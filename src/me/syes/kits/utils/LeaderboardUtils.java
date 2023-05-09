package me.syes.kits.utils;

import java.io.File;
import java.io.IOException;

import me.syes.kits.leaderboard.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.syes.kits.Kits;

public class LeaderboardUtils {
	
	public static void saveLeaderboardData() {
		File f = new File(Kits.getInstance().getDataFolder() + "/Leaderboards" + ".yml");
		
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration fc = new YamlConfiguration();
		try {
			fc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		int i = 0;
		for(String key : fc.getKeys(false))
			fc.set(key, null);
		for(Leaderboard lb : Kits.getInstance().getLeaderboardManager().getLeaderboards()) {
			if(!lb.hasBeenDeleted()) {
				fc.set(i + ".Type", lb.getLeaderboardType().toString());
				fc.set(i + ".Location.World", lb.getLocation().getWorld().getName());
				fc.set(i + ".Location.X", lb.getLocation().getX());
				fc.set(i + ".Location.Y", lb.getLocation().getY());
				fc.set(i + ".Location.Z", lb.getLocation().getZ());
				//lb.removeLeaderboard(false);
			}else if(lb.hasBeenDeleted()){
				fc.set("" + i, null);
			}
			i++;
		}
		
		try {
			fc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadLeaderboardData() {
		File f = new File(Kits.getInstance().getDataFolder() + "/Leaderboards.yml");
		if(!f.exists())
			return;
		FileConfiguration fc = new YamlConfiguration();
		try {
			fc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		for(String str : fc.getKeys(false)) {
			if(fc.getString(str + ".Type") == null)
				new KillsLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
					, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("kills"))
				new KillsLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
					, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("exp"))
				new ExpLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
					, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("events"))
				new EventLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
					, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("killstreak"))
				new KillStreakLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
					, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("kdr"))
				new KDRLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
					, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("deaths"))
				new DeathsLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
						, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("eventsplayed"))
				new EventsPlayedLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
						, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
			else if(fc.getString(str + ".Type").equalsIgnoreCase("eventexp"))
				new EventExpLeaderboard(new Location(Bukkit.getWorld(fc.getString(str + ".Location.World"))
						, fc.getDouble(str + ".Location.X"), fc.getDouble(str + ".Location.Y"), fc.getDouble(str + ".Location.Z")));
		}
	}

}
