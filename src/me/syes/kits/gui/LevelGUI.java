package me.syes.kits.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.experience.ExpLevel;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ItemUtils;

public class LevelGUI {
	
	public static void openLevelGUI(Player p, KitPlayer kp) {
		Inventory inv = Bukkit.createInventory(null, ((Kits.getInstance().getExpManager().getLevels().size()/9)+1) * 9, "§8Your Level Progress:");
		for(ExpLevel level : Kits.getInstance().getExpManager().getLevels()) {
			List<String> lore = new ArrayList<String>();
			if(kp.getExp() >= level.getRequiredExp()) {
				lore.add("§fLevel: " + level.getPrefix());
				lore.add("§fProgress: §aComplete!");
				inv.setItem(inv.firstEmpty()
						, ItemUtils.buildItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), level.getPrefix(), lore, false, false));
			}else if(Kits.getInstance().getExpManager().getExpForNextLevel(kp) == level.getRequiredExp()){
				lore.add("§fLevel: §c???");
				lore.add("§fProgress: §a" + (kp.getExp()-Kits.getInstance().getExpManager().getLevel(kp.getExp()).getRequiredExp())
                		+ "/" + (Kits.getInstance().getExpManager().getExpForNextLevel(kp)
                		-Kits.getInstance().getExpManager().getLevel(kp.getExp()).getRequiredExp()) + " Exp");
				lore.add(Kits.getInstance().getExpManager().getProgressBar(kp, 12, "&a"));
				inv.setItem(inv.firstEmpty()
						, ItemUtils.buildItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4), "§c???", lore, false, false));
			}else {
				lore.add("§fLevel: §c???");
				lore.add("§fProgress: §c???");
				inv.setItem(inv.firstEmpty()
						, ItemUtils.buildItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), "§c???", lore, false, false));
			}
			lore.clear();
		}
		
		
		//Open the GUI
		p.openInventory(inv);
		Kits.getInstance().getGuiManager().setInGUI(p, true);
	}

}
