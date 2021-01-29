package me.syes.kits.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.syes.kits.kit.Kit;

public class ItemUtils {

	public static ItemStack buildHead(String name) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta sm = (SkullMeta) head.getItemMeta();
		sm.setOwner(name);
		head.setItemMeta(sm);
		return head;
	}

	public static ItemStack buildItem(ItemStack i, String name, List<String> lore, boolean hideEnchants, boolean hidePotionEffects) {
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name.replace("&", "§"));
		im.setLore(lore);
		if(hideEnchants)
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		i.setItemMeta(im);
		return i;
	}

	public static ItemStack buildEnchantedItem(ItemStack i, String name, List<String> lore, HashMap<Enchantment, Integer> enchants) {
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name.replace("&", "§"));
		im.setLore(lore);
		for(Enchantment es : enchants.keySet())
			im.addEnchant(es, enchants.get(es), true);
		i.setItemMeta(im);
		return i;
	}
	
	public static void renameItems(Collection<ItemStack> items, Kit k, String prefix) {
		for(ItemStack i : items) {
			i = nameItem(i, k.getName(), prefix);
		}
	}
	
	public static ItemStack nameItem(ItemStack i, String kit, String prefix) {
		if(i != null
				&& !i.getType().equals(Material.GOLDEN_APPLE)
				&& !i.getType().equals(Material.POTION)
				&& !i.getType().equals(Material.MONSTER_EGG)
				&& !i.getType().equals(Material.ARROW)
				&& !i.getType().equals(Material.EGG)
				&& !i.getType().equals(Material.SNOW_BALL)) {
			ItemMeta im = i.getItemMeta();
			if(im == null) return new ItemStack(Material.AIR);
			im.setDisplayName(prefix.replace("&", "§") + "§f" + kit + "'s " + getItemStackName(i));
			i.setItemMeta(im);
		}
		return i;
	}
	
	public static String getItemStackName(ItemStack is) {
		if(is == null) return "";
		String fullname = is.getType().toString().toLowerCase().replace("_", " ");
		String[] split1 = fullname.split(" ");
		String itemname = "";
		for(int i = 0; i < split1.length; i++) {
			String[] split2 = split1[i].split("");
			for(int z = 0; z < split2.length; z++) {
				if(z == 0) {
					itemname = itemname + split2[z].toUpperCase();
					continue;
				}
				itemname = itemname + split2[z];
			}
			if(i < split1.length-1)
				itemname = itemname + " ";
		}
		return itemname;
	}


}
