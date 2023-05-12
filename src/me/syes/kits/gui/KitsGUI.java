package me.syes.kits.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.syes.kits.kit.KitManager;
import me.syes.kits.utils.*;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.kitplayer.KitPlayer;

public class KitsGUI {
	
	public static void openKitsGUI(Player p) {
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		KitManager km = Kits.getInstance().getKitManager();
		Inventory inv = Bukkit.createInventory(null, (int)(((km.getKits().size()+1)/9)+3) * 9 , "§a§lAvailable Kits:");
		List<String> lore = new ArrayList<>();
		for(Kit k : km.getKits()) {
			if(k.getLevel() > 1)
				continue;
			while(k.hasUpgrade()){
				if(kp.getExp() >= km.getKit(k.getName(), k.getLevel()+1).getRequiredExp())
					k = km.getKit(k.getName(), k.getLevel()+1);
				else
					break;
			}
//			Kit upgradedKit = null;
//			if(k.hasUpgrade())
//				upgradedKit = km.getKit(k.getName(), k.getLevel()+1);
//			if(kp.getExp() > upgradedKit.getRequiredExp())
//			if(k.getP)
//				continue;
//			if(k.hasPrestige() && kp.getExp() > km.getKit(k.getName() + "_prestige").getRequiredExp())
//				k = km.getKit(k.getName() + "_prestige");
			if(!p.hasPermission("kits." + k.getName().toLowerCase()) && ConfigUtils.getConfigSection("Kits").getBoolean("Per-Kit-Permission"))
				lore.add("§cKit Locked");
			else if(kp.getExp() < k.getRequiredExp()) {
				lore.add("§7Progress (Unlock): §a" + kp.getExp() + "/" + k.getRequiredExp() + " Exp");
				lore.add("§7");
			}else if(k.hasUpgrade()) {
				Kit nextLevel = km.getKit(k.getName(), k.getLevel()+1);
				lore.add("§7Progress (" + TextUtils.toRoman(k.getLevel()) + " \u27A1 " + TextUtils.toRoman(nextLevel.getLevel()) + "): §b" + kp.getExp() + "/" + nextLevel.getRequiredExp() + " Exp");
				lore.add("§7");
			}
			for(ItemStack i : k.getItems().values()) {
				if((i.getMaxStackSize() > 1 || i.getAmount() > 1) && !i.getType().equals(Material.POTION))
					lore.add("§8\u25aa §7" + ItemUtils.getItemStackName(i) + " §8x" + i.getAmount());
				else if((i.getMaxStackSize() == 1 || i.getAmount() == 1) && !i.getType().equals(Material.POTION))
					lore.add("§8\u25aa §e" + ItemUtils.getItemStackName(i));
				else
					if(i.getDurability() > 16000)
						lore.add("§8\u25aa §7Splash " + ItemUtils.getItemStackName(i) + " §8x" + i.getAmount());
					else
						lore.add("§8\u25aa §7" + ItemUtils.getItemStackName(i) + " §8x" + i.getAmount());
				if(i.getItemMeta().hasEnchants())
					for(Enchantment e : i.getItemMeta().getEnchants().keySet()) {
						lore.add("  §8\u25aa " + EnchantUtils.translateEnchantment(e) + " " + i.getItemMeta().getEnchantLevel(e));
					}
				if((i.getType().getMaxDurability()-i.getDurability() < i.getType().getMaxDurability()) && !i.getType().equals(Material.POTION) && !i.getType().equals(Material.MONSTER_EGG)) {
					lore.add("  §8\u25aa " + (i.getType().getMaxDurability()-i.getDurability()) + " Durability");
				}
				if(i.getType().equals(Material.POTION)) {
					PotionMeta pm = (PotionMeta) i.getItemMeta();
					if(!pm.hasCustomEffects())
						lore.add("  §8\u25aa " + PotionUtils.translatePotionID(i.getDurability()));
					for(PotionEffect pe : pm.getCustomEffects()) {
						if(pe.getDuration() < 20)
							lore.add("  §8\u25aa " + PotionUtils.translatePotionEffect(pe.getType()) + " " + (pe.getAmplifier()+1));
						else
							lore.add("  §8\u25aa " + PotionUtils.translatePotionEffect(pe.getType()) + " " + (pe.getAmplifier()+1) + " §8(" + TimeUtils.format(pe.getDuration()/20) + ")");
					}
				}
			}
			for(int x = k.getArmour().length-1; x >= 0; x--)
				if(k.getArmour()[x].getType() != Material.AIR) {
					lore.add("§8\u25aa §9" + ItemUtils.getItemStackName(k.getArmour()[x]));
					if(k.getArmour()[x].getItemMeta().hasEnchants())
						for(Enchantment e : k.getArmour()[x].getItemMeta().getEnchants().keySet()) {
							lore.add("  §8\u25aa " + EnchantUtils.translateEnchantment(e) + " " + k.getArmour()[x].getItemMeta().getEnchantLevel(e));
						}
					if((k.getArmour()[x].getType().getMaxDurability()-k.getArmour()[x].getDurability() < k.getArmour()[x].getType().getMaxDurability()) && !k.getArmour()[x].getType().equals(Material.SKULL_ITEM)) {
						lore.add("  §8\u25aa " + (k.getArmour()[x].getType().getMaxDurability()-k.getArmour()[x].getDurability()) + " Durability");
					}
				}
			if(kp.getSavedKits().contains(k.getName())) {
				lore.add("§7");
				lore.add("§aYou have saved this kit.");
			}
			if(kp.getExp() < k.getRequiredExp())
				inv.addItem(ItemUtils.buildItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), "§c" + k.getName() + " (" + TextUtils.toRoman(k.getLevel()) + ")", lore, true, true));
			else
				inv.addItem(ItemUtils.buildItem(k.getIcon(), "§a" + k.getName() + " (" + TextUtils.toRoman(k.getLevel()) + ")", lore, true, true));
			lore.clear();
		}

		List<Integer> slots = Arrays.asList(inv.getSize()-8, inv.getSize()-7, inv.getSize()-3, inv.getSize()-2);
		for(int i = 0; i < slots.size(); i++)
				inv.setItem(slots.get(i), ItemUtils.buildItem(new ItemStack(Material.EMPTY_MAP, 1)
						, "§aEmpty Kit Slot", Arrays.asList("§7Right-Click a kit", "§7in order to save it."), false, false));

		for(String str : kp.getSavedKits()) {
			Kit k = km.getKit(str);
			while(k.hasUpgrade()){
				if(kp.getExp() > km.getKit(k.getName(), k.getLevel()+1).getRequiredExp())
					k = km.getKit(k.getName(), k.getLevel()+1);
				else
					break;
			}
			if(k == null) {
				kp.removeSavedKit(str);
				continue;
			}
			for(int i : slots)
				if(inv.getItem(i).getType().equals(Material.EMPTY_MAP)) {
					inv.setItem(i, ItemUtils.buildItem(k.getIcon()
							, "§a" + k.getName(), Arrays.asList("§7Shift + Left-Click to", "§7remove this kit."), true, true));
					break;
				}
		}

		inv.setItem(inv.getSize()-5, ItemUtils.buildItem(new ItemStack(Material.PAINTING)
				, "§aRandom Kit", Arrays.asList("§7Gives you a random kit.", "§7(Only unlocked kits)"), true, false));
		
		//TODO clean up code and update inventory upon saving a kit or resetting a saved kit
		
		/*new BukkitRunnable() {
			public void run() {
				if(!Kits.getInstance().getGuiManager().isInGUI(p)) {
					this.cancel();
					return;
				}
				inv.setItem(inv.getSize()-5, ItemUtils.buildItem(Kits.getInstance().getKitManager().getRandomKit().getIcon()
						, "§aRandom Kit", Arrays.asList("§7Gives you a random kit.", "§7(Only unlocked kits)"), true, false));
				p.openInventory(inv);
				Kits.getInstance().getGuiManager().setInGUI(p, true);
			}
		}.runTaskTimer(Kits.getInstance(), 0, 15);*/
		
		//Open the GUI
		p.openInventory(inv);
		Kits.getInstance().getGuiManager().setInGUI(p, true);
	}

}
