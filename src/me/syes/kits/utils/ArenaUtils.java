package me.syes.kits.utils;

import me.syes.kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ArenaUtils {

	public static int calculateLowestBlock(World world, int xValue, int zValue, int minY, int maxY){
		for(int i = maxY; i >= minY; i--){
			if(!world.getBlockAt(xValue, i, zValue).getType().equals(Material.AIR) && world.getBlockAt(xValue, i+1, zValue).getType().equals(Material.AIR)
			&& world.getBlockAt(xValue, i+2, zValue).getType().equals(Material.AIR))
				return i;
		}
		for(int i = maxY+1; i < 255; i++){
			if(!world.getBlockAt(xValue, i, zValue).getType().equals(Material.AIR) && world.getBlockAt(xValue, i+1, zValue).getType().equals(Material.AIR)
					&& world.getBlockAt(xValue, i+2, zValue).getType().equals(Material.AIR))
				return i;
		}
		return -1;
	}
	
	public static void saveArenaData() {
			File f = new File(Kits.getInstance().getDataFolder() + "/Arena" + ".yml");
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
			
			fc.set("World", Kits.getInstance().arenaManager.getArena().getWorld().getName());
			fc.set("Bounds.Min.X", Kits.getInstance().arenaManager.getArena().getMinBounds().getBlockX());
			fc.set("Bounds.Min.Y", Kits.getInstance().arenaManager.getArena().getMinBounds().getBlockY());
			fc.set("Bounds.Min.Z", Kits.getInstance().arenaManager.getArena().getMinBounds().getBlockZ());
			fc.set("Bounds.Max.X", Kits.getInstance().arenaManager.getArena().getMaxBounds().getBlockX());
			fc.set("Bounds.Max.Y", Kits.getInstance().arenaManager.getArena().getMaxBounds().getBlockY());
			fc.set("Bounds.Max.Z", Kits.getInstance().arenaManager.getArena().getMaxBounds().getBlockZ());
			fc.set("Spawn.X", Kits.getInstance().arenaManager.getArena().getLobbySpawn().getBlockX());
			fc.set("Spawn.Y", Kits.getInstance().arenaManager.getArena().getLobbySpawn().getBlockY());
			fc.set("Spawn.Z", Kits.getInstance().arenaManager.getArena().getLobbySpawn().getBlockZ());
			fc.set("Spawn.Pitch", Kits.getInstance().arenaManager.getArena().getLobbySpawn().getPitch());
			fc.set("Spawn.Yaw", Kits.getInstance().arenaManager.getArena().getLobbySpawn().getYaw());
			
			try {
				fc.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void loadArenaData() {
		File f = new File(Kits.getInstance().getDataFolder() + "/Arena" + ".yml");
		if(!f.exists())
			return;
		FileConfiguration fc = new YamlConfiguration();
		try {
			fc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		Kits.getInstance().arenaManager.getArena().setWorld(Bukkit.getWorld(fc.getString("World")));
		Kits.getInstance().arenaManager.getArena().setMinBounds(fc.getInt("Bounds.Min.X"), fc.getInt("Bounds.Min.Y"), fc.getInt("Bounds.Min.Z"));
		Kits.getInstance().arenaManager.getArena().setMaxBounds(fc.getInt("Bounds.Max.X"), fc.getInt("Bounds.Max.Y"), fc.getInt("Bounds.Max.Z"));
		Kits.getInstance().arenaManager.getArena().setLobbySpawn(new Location(Kits.getInstance().arenaManager.getArena().getWorld()
				, fc.getInt("Spawn.X") + 0.5, fc.getInt("Spawn.Y") + 0.5, fc.getInt("Spawn.Z") + 0.5, (int) fc.getDouble("Spawn.Yaw"), (int) fc.getDouble("Spawn.Pitch")));
		
	}

}
