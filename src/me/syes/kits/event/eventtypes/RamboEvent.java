package me.syes.kits.event.eventtypes;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;

public class RamboEvent extends Event {
	
	public RamboEvent(EventManager eventManager) {
		this.eventManager = eventManager;
		this.participants = new HashMap<UUID, Double>();
		this.name = "Rambo";
		this.goal = "Get the most kills to win.";
		this.rules = "Wearing armour does not affect players. Killing a player heals you to full health.";
	}
	
	@Override
	public void startEvent() {
		this.announceEventStart();
		this.loadParticipants();
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

}
