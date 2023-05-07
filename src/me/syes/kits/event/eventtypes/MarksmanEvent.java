package me.syes.kits.event.eventtypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class MarksmanEvent extends Event {

	private List<ItemStack> dropsOnDeath;
	
	public MarksmanEvent(EventManager eventManager) {
		this.dropsOnDeath = Arrays.asList(new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 12));
		
		this.eventManager = eventManager;
		this.participants = new HashMap<UUID, Double>();
		this.name = "Marksman";
		this.goal = "Hit the most bowshots to win.";
		this.rules = "Shots hit from farther away award more points. Players drop a bow and 12 arrows on death. All players receive a bow and 16 arrows.";
	}
	
	@Override
	public void startEvent() {
		this.announceEventStart();
		this.loadParticipants();
		for(UUID uuid : this.getParticipants()) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
			if(Bukkit.getOfflinePlayer(kp.getUuid()).isOnline()) {
				Player p = Bukkit.getPlayer(kp.getUuid());
				onArenaEnter(p);
			}
			
		}
		this.setActive(true);
		this.time = this.durationSeconds;
		new BukkitRunnable() {
			public void run() {
				if(time == 0) {
					finishEvent();
					this.cancel();
					time++;
				}
				time--;
			}
		}.runTaskTimerAsynchronously(Kits.getInstance(), 0, 20);
	}

	@Override
	public void finishEvent() {
		this.announceEventEnd();
		setActive(false);
		this.participants.clear();
		time = durationSeconds;
		resetArena();
	}
	
	public List<ItemStack> getDropsOnDeath() {
		return dropsOnDeath;
	}

	@Override
	public void onArenaEnter(Player p) {
		p.getInventory().addItem(new ItemStack(Material.BOW));
		p.getInventory().addItem(new ItemStack(Material.ARROW, 16));
	}

}
