package me.syes.kits.utils;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	/*public static HashMap<Integer, ItemStack> getInventoryHashMap(Player p) {
		HashMap<Integer, ItemStack> items = new HashMap();
		for(int x = 0; x <= p.getInventory().getSize(); x++) {
			ItemStack i = p.getInventory().getItem(x);
			if(i != null) {
				items.put(x, i.clone());
			}
		}
		return items;
	}*/
	
	public static HashMap<Integer, ItemStack> getNamedInventoryHashMap(Player p, String k, String prefix) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for(int x = 0; x < p.getInventory().getSize(); x++) {
			ItemStack i = p.getInventory().getItem(x);
			if(i != null) {
				i = ItemUtils.nameItem(i.clone(), k, prefix);
				items.put(x, i);
			}
		}
		return items;
	}
	
	/*public static ItemStack[] getArmourArray(Player p) {
		ItemStack[] armour = new ItemStack[4];
		for(int x = 0; x < p.getInventory().getArmorContents().length; x++) {
			ItemStack i = p.getInventory().getArmorContents()[x];
			if(i != null)
				armour[x] = i.clone();
		}
		return armour;
	}*/
	
	public static ItemStack[] getNamedArmourArray(Player p, String k, String prefix) {
		ItemStack[] armour = new ItemStack[4];
		for(int x = 0; x < p.getInventory().getArmorContents().length; x++) {
			ItemStack i = p.getInventory().getArmorContents()[x];
			if(i != null)
				i = ItemUtils.nameItem(i.clone(), k, prefix);
				if(i != null) armour[x] = i;
		}
		return armour;
	}

}
