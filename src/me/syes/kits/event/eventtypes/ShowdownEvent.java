package me.syes.kits.event.eventtypes;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;

public class ShowdownEvent extends Event {
	
	public ShowdownEvent(EventManager eventManager) {
		this.eventManager = eventManager;
		this.participants = new HashMap<UUID, Double>();
		this.name = "Showdown";
		this.goal = "Deal the most damage against players to win.";
		this.rules = "All players' health is doubled. Players drop 1 Golden Apple on death.";
	}
	
	@Override
	public void startEvent() {
		this.announceEventStart();
		this.loadParticipants();
		for(UUID uuid : this.getParticipants()) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
			if(Bukkit.getOfflinePlayer(kp.getUuid()).isOnline()) {
				Player p = Bukkit.getPlayer(kp.getUuid());
				setDoubleHealth(p);
			}
			
		}
		this.setActive(true);
		this.time = this.durationSeconds;
		new BukkitRunnable() {
			public void run() {
				if(!isActive()) {
					this.cancel();
					return;
				}
				if(time == 0) {
					finishEvent();
					this.cancel();
					time++;
				}
				time--;
			}
		}.runTaskTimer(Kits.getInstance(), 0, 20);
	}
	
	public void setDoubleHealth(Player p) {
		p.setMaxHealth(40);
		p.setHealth(p.getMaxHealth());
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
