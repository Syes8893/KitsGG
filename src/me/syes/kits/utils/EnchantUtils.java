package me.syes.kits.utils;

import org.bukkit.enchantments.Enchantment;

public class EnchantUtils {
	
	public static String translateEnchantment(Enchantment e) {
		if(e.getName().equals("DAMAGE_ALL")) return "Sharpness";
		if(e.getName().equals("KNOCKBACK")) return "Knockback";
		if(e.getName().equals("FIRE_ASPECT")) return "Fire Aspect";
		if(e.getName().equals("DAMAGE_UNDEAD")) return "Smite";
		if(e.getName().equals("DAMAGE_ARTHROPODS")) return "Bane of Arthropods";
		if(e.getName().equals("LOOT_BONUS_MOBS")) return "Looting";
		if(e.getName().equals("ARROW_DAMAGE")) return "Power";
		if(e.getName().equals("ARROW_KNOCKBACK")) return "Punch";
		if(e.getName().equals("ARROW_FIRE")) return "Flame";
		if(e.getName().equals("ARROW_INFINITE")) return "Infinity";
		if(e.getName().equals("PROTECTION_ENVIRONMENTAL")) return "Protection";
		if(e.getName().equals("PROTECTION_PROJECTILE")) return "Projectile Protection";
		if(e.getName().equals("PROTECTION_FIRE")) return "Fire Protection";
		if(e.getName().equals("PROTECTION_EXPLOSIONS")) return "Blast Protection";
		if(e.getName().equals("PROTECTION_FALL")) return "Feather Falling";
		if(e.getName().equals("OXYGEN")) return "Respiration";
		if(e.getName().equals("WATER_WORKER")) return "Aqua Affinity";
		if(e.getName().equals("THORNS")) return "Thorns";
		if(e.getName().equals("DEPTH_STRIDER")) return "Depth Strider";
		if(e.getName().equals("DURABILITY")) return "Unbreaking";
		return e.getName();
	}

}
