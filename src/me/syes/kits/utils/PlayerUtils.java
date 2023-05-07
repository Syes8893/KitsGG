package me.syes.kits.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;

public class PlayerUtils {
	
	public static void savePlayerData() {
		for(KitPlayer kp : Kits.getInstance().playerManager.getKitPlayers().values()) {
			//new File(Kits.getInstance().getDataFolder() + "/players").mkdir();
			File f = new File(Kits.getInstance().getDataFolder() + "/players/" + kp.getUuid() + ".yml");
			
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
			
			fc.set("Kills", kp.getKills());
			fc.set("Killstreak", kp.getKillstreak());
			fc.set("Highestkillstreak", kp.getHighestKillstreak());
			fc.set("Deaths", kp.getDeaths());
			fc.set("inArena", kp.isInArena());
			fc.set("Arrowsshot", kp.getArrowsShot());
			fc.set("Arrowshit", kp.getArrowsHit());
			fc.set("Potionsdrunk", kp.getPotionsDrunk());
			fc.set("Potionsthrown", kp.getPotionsThrown());
			fc.set("Heartshealed", kp.getHeartsHealed());
			fc.set("Eventsplayed", kp.getEventsPlayed());
			fc.set("Eventswon", kp.getEventsWon());
			fc.set("Bonusexp", kp.getBonusExp());
			fc.set("Savedkits", kp.getSavedKits().toArray());
			fc.set("Unlockedkits", kp.getUnlockedKits().toArray());
			
			try {
				fc.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadPlayerData() {
		new File(Kits.getInstance().getDataFolder() + "/players").mkdir();
		for(File f : new File(Kits.getInstance().getDataFolder() + "/players").listFiles()) {
			FileConfiguration fc = new YamlConfiguration();
			try {
				fc.load(f);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			KitPlayer kp = new KitPlayer(UUID.fromString(f.getName().replace(".yml", "")), fc);
			//KitPlayer kp = new KitPlayer(UUID.fromString(f.getName().replace(".yml", "")), fc.getInt("Kills"), fc.getInt("Deaths"), fc.getBoolean("inArena"));
		}
	}

}
