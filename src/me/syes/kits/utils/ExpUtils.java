package me.syes.kits.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.syes.kits.Kits;
import me.syes.kits.experience.ExpLevel;
import me.syes.kits.experience.ExpManager;

public class ExpUtils {
	
	public static void loadExpLevels() {
		File f = new File(Kits.getInstance().getDataFolder() + "/Levels.yml");
		if(!f.exists()) {
			Kits.getInstance().saveResource(f.getName(), true);
		}
		FileConfiguration fc = new YamlConfiguration();
		try {
			fc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		for(String str : fc.getKeys(false)) {
			new ExpLevel(fc.getInt(str + ".requiredExp"), fc.getString(str + ".name")
					, fc.getString(str + ".primaryColor"), fc.getString(str + ".secondaryColor"), fc.getString(str + ".nameColor")
					, Kits.getInstance().getExpManager());
		}
	}

}
