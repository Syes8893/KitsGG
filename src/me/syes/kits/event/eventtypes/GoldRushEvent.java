package me.syes.kits.event.eventtypes;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.arena.Arena;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;

public class GoldRushEvent extends Event {
	
	public GoldRushEvent(EventManager eventManager) {
		this.eventManager = eventManager;
		this.participants = new HashMap<UUID, Double>();
		this.name = "Gold Rush";
		this.goal = "Collect the most Gold Nuggets to win.";
		this.rules = "Players drop 1 Gold Nuggets on death, killing a player with a killstreak will drop additional Gold Nuggets. Additionally, Gold Nuggets will randomly drop in the arena.";
	}
	
	@Override
	public void startEvent() {
		this.announceEventStart();
		this.loadParticipants();
		this.setActive(true);
		this.time = this.durationSeconds;
		new BukkitRunnable() {
			public void run() {
				if(!isActive()) {
					this.cancel();
					return;
				}
				if(time % 3 == 0 && time > 0) {
					Arena a = Kits.getInstance().getArenaManager().getArena();
					for(int i = 0; i < Bukkit.getServer().getOnlinePlayers().size(); i++){
						new BukkitRunnable() {
							@Override
							public void run() {
								a.getWorld().dropItemNaturally(a.getRandomSpawn().add(0, 1.5, 0), new ItemStack(Material.GOLD_NUGGET));
							}
						}.runTask(Kits.getInstance());
					}
				} else if(time == 0) {
					finishEvent();
					this.cancel();
					time++;
				} else if(!active) {
					this.cancel();
					time++;
				}
				time--;
			}
		}.runTaskTimer(Kits.getInstance(), 0, 20);
	}

	@Override
	public void finishEvent() {
		this.announceEventEnd();
		setActive(false);
		this.participants.clear();
		time = durationSeconds;
		resetArena();
	}

}
