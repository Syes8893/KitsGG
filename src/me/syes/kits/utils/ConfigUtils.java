package me.syes.kits.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.syes.kits.Kits;

public class ConfigUtils {
	
	public static FileConfiguration config;
	final public static String configVersion = "1.2.1";

	public static boolean perKitPermissions;

	public static void loadConfig() {
		Kits.getInstance().saveDefaultConfig();
		config = Kits.getInstance().getConfig();
		perKitPermissions = ConfigUtils.getConfigSection("Kits").getBoolean("Per-Kit-Permission");
		if(!config.getString("Config-Version").equals(configVersion))
			updateConfig();
	}
	
	public static void updateConfig() {
        File configFile = new File(Kits.getInstance().getDataFolder(), "config.yml");
        File oldConfigFile = new File(Kits.getInstance().getDataFolder(), "config_old.yml");
        configFile.renameTo(oldConfigFile);
        if (configFile.exists()) {
            configFile.delete();
        }
        Bukkit.getServer().getLogger().info("[" + Kits.getInstance().getDescription().getName()
        		+ "] A new config version has been detected, old config has been saved to config_old.yml.");
        Kits.getInstance().saveDefaultConfig();
	}
	
	public static ConfigurationSection getConfigSection(String section) {
		return config.getConfigurationSection(section + ".");
	}
	
	public static FileConfiguration getConfig() {
		return config;
	}
	
}
