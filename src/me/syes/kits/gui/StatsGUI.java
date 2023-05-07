package me.syes.kits.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ItemUtils;

public class StatsGUI {
	
	public static void openStatsGUI(Player p, KitPlayer kp) {
		Inventory inv = Bukkit.createInventory(null, 27, Bukkit.getOfflinePlayer(kp.getUuid()).getName() + "'s Stats:");
		List<String> lore = new ArrayList<String>();
		lore.add("§fKills: §a" + kp.getKills());
		lore.add("§fHighest Killstreak: §a" + kp.getHighestKillstreak());
		lore.add("§fDeaths: §a" + kp.getDeaths());
		lore.add("§fK/D: §a" + new DecimalFormat("#.##").format(kp.getKDR()));
		lore.add("§f");
		lore.add("§fLevel: " + Kits.getInstance().getExpManager().getLevel(kp.getExp()).getPrefix());
		lore.add("§fTotal Exp: §a" + kp.getRawExp() + " §d(+" + kp.getBonusExp() + " Bonus)");
		if((Kits.getInstance().getExpManager().getExpForNextLevel(kp)-kp.getExp()) < 0)
			lore.add("§fNext Level: §7MAX LEVEL");
		else
			lore.add("§fNext Level: §a" + (Kits.getInstance().getExpManager().getExpForNextLevel(kp)-kp.getExp()) + " Exp");
		ItemStack combat = ItemUtils.buildItem(new ItemStack(Material.IRON_SWORD), "&a&lCombat Stats", lore, true, true);
		lore.clear();
		lore.add("§fArrows Shot: §a" + kp.getArrowsShot());
		lore.add("§fArrows Hit: §a" + kp.getArrowsHit());
		ItemStack bow = ItemUtils.buildItem(new ItemStack(Material.ARROW), "&a&lBow Stats", lore, true, true);
		lore.clear();
		lore.add("§fPotions Drunk: §a" + kp.getPotionsDrunk());
		lore.add("§fPotions Thrown: §a" + kp.getPotionsThrown());
		ItemStack potions = ItemUtils.buildItem(new ItemStack(Material.BREWING_STAND_ITEM), "&a&lPotion Stats", lore, true, true);
		lore.clear();
		lore.add("§fHearts Healed: §a" + kp.getHeartsHealed() + "\u2764");
		ItemStack misc = ItemUtils.buildItem(new ItemStack(Material.BOOK_AND_QUILL), "&a&lMisc Stats", lore, true, true);
		lore.clear();
		lore.add("§fEvents Played: §a" + kp.getEventsPlayed());
		lore.add("§fEvents Won: §a" + kp.getEventsWon());
		ItemStack events = ItemUtils.buildItem(new ItemStack(Material.CAKE), "&a&lEvent Stats", lore, true, true);
		
		//Set all items in the GUI
		inv.setItem(10, misc);
		inv.setItem(11, bow);
		inv.setItem(13, combat);
		inv.setItem(15, potions);
		inv.setItem(16, events);
		
		//Open the GUI
		p.openInventory(inv);
		Kits.getInstance().getGuiManager().setInGUI(p, true);
	}

}
