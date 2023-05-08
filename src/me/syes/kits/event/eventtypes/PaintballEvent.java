package me.syes.kits.event.eventtypes;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PaintballEvent extends Event {

	public PaintballEvent(EventManager eventManager) {
		this.eventManager = eventManager;
		this.participants = new HashMap<UUID, Double>();
		this.name = "Paintball";
		this.goal = "Score the most points to win.";
		this.rules = "Players receive 3 snowballs every 3 seconds. Snowballs deal 2 hearts of damage. Hitting a player with a snowball awards 1 point. Killing a player with a snowball awards 3 points.";
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
				if(time% 3 == 0)
					for(UUID uuid : participants.keySet()){
						if(Bukkit.getOfflinePlayer(uuid).isOnline()){
							KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
							if(kp.isInArena())
								Bukkit.getPlayer(uuid).getInventory().addItem(new ItemStack(Material.SNOW_BALL, 3));
						}
					}
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

	@Override
	public void onArenaEnter(Player p) {
		super.onArenaEnter(p);
	}

}
