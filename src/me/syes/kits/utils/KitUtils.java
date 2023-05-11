package me.syes.kits.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;

public class KitUtils {
	
	public static void saveKits() {
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			saveKit(k);
		}
	}
	
	public static void saveKit(Kit k) {
		new File(Kits.getInstance().getDataFolder() + "/kits").mkdir();
		File f = new File(Kits.getInstance().getDataFolder() + "/kits/" + k.getFileName() + ".yml");
		
		if(!f.exists() && !f.getName().equals(k.getFileName())) {
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
		
		//Reset the file in order to remove all items that were removed during a possible kit change
		for(String str : fc.getKeys(false)) {
			fc.set(str, null);
		}
		
		for(int i : k.getItems().keySet()) {
			fc.set(k.getName().toLowerCase() + ".items." + i, k.getItems().get(i));
		}
		for(int i = 0; i < k.getArmour().length; i++) {
			fc.set(k.getName().toLowerCase() + ".armour." + i, k.getArmour()[i]);
		}
		fc.set(k.getName().toLowerCase() + ".icon", k.getIcon());
		fc.set(k.getName().toLowerCase() + ".requiredExp", k.getRequiredExp());
		fc.set(k.getName().toLowerCase() + ".hasUpgrade", k.hasUpgrade());
		fc.set(k.getName().toLowerCase() + ".level", k.getLevel());
		
		try {
			fc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadKits() {
		if(!new File (Kits.getInstance().getDataFolder() + "/kits").exists())
			return;
		for(File f : new File(Kits.getInstance().getDataFolder() + "/kits").listFiles()) {
			FileConfiguration fc = new YamlConfiguration();
			try {
				fc.load(f);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			new Kit(getKitName(fc), loadItems(fc), loadArmour(fc), loadIcon(fc), fc.getInt(getKitName(fc).toLowerCase() + ".requiredExp"), getHasUpgrade(fc), getLevel(fc));
		}

		Kits.getInstance().getKitManager().organiseKits();
	}
	
	public static String getKitName(FileConfiguration fc) {
		return fc.getString("");
	}

	public static Boolean getHasUpgrade(FileConfiguration fc) {
		return fc.getBoolean(getKitName(fc).toLowerCase() + ".hasUpgrade");
	}

	public static int getLevel(FileConfiguration fc) {
		return fc.getInt(getKitName(fc).toLowerCase() + ".level");
	}

	public static HashMap<Integer, ItemStack> loadItems(FileConfiguration fc) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for(String str : fc.getKeys(false)) {
			for(String str2 : fc.getConfigurationSection(str).getKeys(false)) {
				if(str2.equalsIgnoreCase("items")) {
					for(String str3 : fc.getConfigurationSection(str + "." + str2).getKeys(false)) {
						items.put(Integer.parseInt(str3), (ItemStack) fc.get(str + "." + str2 + "." + str3));
					}
				}
			}
		}
		return items;
	}

	public static ItemStack[] loadArmour(FileConfiguration fc) {
		ItemStack[] armour = new ItemStack[4];
		for(String str : fc.getKeys(false)) {
			for(String str2 : fc.getConfigurationSection(str).getKeys(false)) {
				if(str2.equalsIgnoreCase("armour")) {
					for(String str3 : fc.getConfigurationSection(str + "." + str2).getKeys(false)) {
						armour[Integer.parseInt(str3)] = (ItemStack) fc.get(str + "." + str2 + "." + str3);
					}
				}
			}
		}
		return armour;
	}
	
	public static ItemStack loadIcon(FileConfiguration fc) {
		for(String str : fc.getKeys(false)) {
			for(String str2 : fc.getConfigurationSection(str).getKeys(false)) {
				if(str2.equalsIgnoreCase("icon")) {
					return fc.getItemStack(str + "." + str2);
				}
			}
		}
		return new ItemStack(Material.COBBLESTONE);
	}
	
}
