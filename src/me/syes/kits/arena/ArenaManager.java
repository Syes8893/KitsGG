package me.syes.kits.arena;

import java.text.DecimalFormat;
import java.util.Arrays;

import me.syes.kits.gui.KitsGUI;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ActionBarMessage;
import me.syes.kits.utils.ItemUtils;

public class ArenaManager {

	public Arena arena;
	
	public ArenaManager() {
		this.arena = new Arena(0, 0, 0, 255, 0, 0, null, null);
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public void warpIntoArena(Player p) {
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(kp.isInArena()) {
			p.sendMessage("§cYou are already in the arena.");
			return;
		}
		if(!kp.hasKitSelected()) {
			p.sendMessage("§cPlease select a kit before entering the arena. Use /kit for a list of kits.");
			KitsGUI.openKitsGUI(p);
			return;
		}
		giveTrackingCompass(p);
		p.setGameMode(GameMode.SURVIVAL);
		p.teleport(this.getArena().getRandomSpawn());
		kp.setInArena(true);
		if(Kits.getInstance().getEventManager().getActiveEvent() == null)
			return;
		Kits.getInstance().getEventManager().getActiveEvent().onArenaEnter(p);
	}

	private void giveTrackingCompass(Player p) {
		ItemStack compass = ItemUtils.buildItem(new ItemStack(Material.COMPASS), "&aTracking Compass &7(Right-Click)", Arrays.asList("§7Tracks the nearest player."), false, false);
		p.getInventory().addItem(compass);
	}
	
	public void setCompassTarget(Player p) {
		if(Kits.getInstance().getEventManager().getBossEvent().isActive())
			return;
		for(Entity e : p.getNearbyEntities(200, 100, 200))
			if(e instanceof Player) {
				if(Kits.getInstance().getPlayerManager().getKitPlayer(((Player)e).getUniqueId()).isInArena()) {
					p.setCompassTarget(e.getLocation());
					ActionBarMessage.sendMessage(p, "§fNow Tracking: §a" + e.getName()
					+ " §7(" + new DecimalFormat("#.#").format(p.getLocation().distance(e.getLocation())) + " Blocks)");
					return;
				}
			}
		ActionBarMessage.sendMessage(p, "§cCouldn't find any players to track.");
	}
	
}
