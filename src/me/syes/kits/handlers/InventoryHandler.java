package me.syes.kits.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.syes.kits.Kits;
import me.syes.kits.gui.KitsGUI;
import me.syes.kits.kit.Kit;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class InventoryHandler implements Listener {
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Kits.getInstance().getGuiManager().setInGUI((Player) 
			e.getPlayer(), false);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(Kits.getInstance().getGuiManager().isInGUI(p))
				e.setCancelled(true);
		if(e.getInventory().getName() == "§a§lAvailable Kits:")
			if(e.getRawSlot() < e.getInventory().getSize() && e.getRawSlot() > -1)
				if(e.getInventory().getItem(e.getSlot()) != null) {
					if(Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId()).isInArena()) {
						p.sendMessage("§cYou can't select a kit whilst in the arena.");
						return;
					}
					String kitName = e.getInventory().getItem(e.getRawSlot()).getItemMeta().getDisplayName().replace("§a", "").replace("§c", "").replace("§b", "");
//					if(!e.isRightClick()) {
//						if(e.isLeftClick() && e.isShiftClick()) {
//							if(!kp.getSavedKits().contains(kitName)) {
//								p.sendMessage("§cYou don't have this kit saved!");
//								return;
//							}
//							if(e.getInventory().getItem(e.getRawSlot()).getType().equals(Material.EMPTY_MAP)) {
//								p.sendMessage("§cThere's no kit saved in this slot!");
//								return;
//							}
//							kp.removeSavedKit(kitName);
//							p.sendMessage("§aSuccesfully unsaved the " + kitName + " Kit. §7(" + kp.getSavedKits().size() + "/4 Saved)");
//							KitsGUI.openKitsGUI(p);
//							return;
//						}
//						for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
//							if(kitName.equalsIgnoreCase(kit.getName())) {
//								Kits.getInstance().getKitManager().giveKit(p, kit);
//								return;
//							}
//						}
//						if(kitName.equalsIgnoreCase("Random Kit")) {
//							Kits.getInstance().getKitManager().giveRandomKit(p);
//							return;
//						}
//					} else {
//						if(!Kits.getInstance().getKitManager().isAllowedKit(p, Kits.getInstance().getKitManager().getKit(kitName), false)) {
//							p.sendMessage("§cYou can't save this kit!");
//							return;
//						}
//						if(kp.getSavedKits().contains(kitName)) {
//							p.sendMessage("§cYou have already saved this kit!");
//							return;
//						}
//						if(kp.getSavedKits().size() == 4) {
//							p.sendMessage("§cYou can't save any more kits! §7(4/4)");
//							return;
//						}
//						kp.addSavedKit(kitName);
//						p.sendMessage("§aSuccesfully saved the " + kitName + " Kit. §7(" + kp.getSavedKits().size() + "/4 Saved)");
//						KitsGUI.openKitsGUI(p);
//						return;
//					}
				}
	}

}
