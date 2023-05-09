package me.syes.kits.event.eventtypes;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SkyFightEvent extends Event {

    public SkyFightEvent(EventManager eventManager) {
        this.eventManager = eventManager;
        this.participants = new HashMap<UUID, Double>();
        this.name = "SkyFight";
        this.goal = "Do the most damage to other players.";
        this.rules = "All players are allowed to fly. If you get hit you fall down.";
    }
    @Override
    public void startEvent() {
        this.announceEventStart();
        this.loadParticipants();
        this.setActive(true);
        this.time = this.durationSeconds;
        for(UUID uuid : this.getParticipants()) {
            KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
            if(kp.isInArena())
                if(Bukkit.getOfflinePlayer(kp.getUuid()).isOnline()) {
                    Player p = Bukkit.getPlayer(kp.getUuid());
                    onArenaEnter(p);
                }
        }
        new BukkitRunnable() {
            public void run() {
                if(time == 0) {
                    finishEvent();
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
        for (UUID uuid : this.participants.keySet()) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                Player p = Bukkit.getPlayer(uuid);
                p.setAllowFlight(false);
                p.setFlying(false);
                p.setFlySpeed(0.1f);
            }
        }
        setActive(false);
        this.participants.clear();
        time = durationSeconds;
        resetArena();
    }
    @Override
    public void onArenaEnter(Player p) {
        super.onArenaEnter(p);
        p.setAllowFlight(true);
        p.setFlySpeed(0.05f);
    }
}
