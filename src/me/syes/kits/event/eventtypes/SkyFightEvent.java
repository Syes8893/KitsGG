package me.syes.kits.event.eventtypes;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ActionBarMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SkyFightEvent extends Event {

    public HashMap<UUID, Integer> flyCooldown;

    public SkyFightEvent(EventManager eventManager) {
        this.eventManager = eventManager;
        this.participants = new HashMap<UUID, Double>();
        this.flyCooldown = new HashMap<UUID, Integer>();
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
            flyCooldown.put(uuid, 0);
            //KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
//            if(kp.isInArena())
                if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
                    Player p = Bukkit.getPlayer(uuid);
                    onArenaEnter(p);
                }
        }
        new BukkitRunnable() {
            public void run() {
                if(!isActive()) {
                    this.cancel();
                    return;
                }
                tickFlightCooldown();
                if(time == 0) {
                    finishEvent();
                    this.cancel();
                    time++;
                }
                time--;
            }
        }.runTaskTimer(Kits.getInstance(), 0, 20);
    }

    private void tickFlightCooldown(){
        for(UUID uuid : flyCooldown.keySet()) {
            if (!Bukkit.getOfflinePlayer(uuid).isOnline()) {
                flyCooldown.put(uuid, -1);
                continue;
            }
            if(!Kits.getInstance().getPlayerManager().getKitPlayer(uuid).isInArena())
                flyCooldown.put(uuid, -1);
            int cooldown = flyCooldown.get(uuid);
            if(cooldown == 0 && Kits.getInstance().getPlayerManager().getKitPlayer(uuid).isInArena()) {
                Bukkit.getPlayer(uuid).setAllowFlight(true);
                ActionBarMessage.sendMessage(Bukkit.getPlayer(uuid), "§aYou can now fly again");
                flyCooldown.put(uuid, -1);
            } else if (cooldown > 0) {
                flyCooldown.put(uuid, cooldown - 1);
                ActionBarMessage.sendMessage(Bukkit.getPlayer(uuid), "§cYou can fly again in " + (cooldown - 1) + " seconds");
            }
        }
    }

    @Override
    public void finishEvent() {
        this.announceEventEnd();
        for (UUID uuid : this.participants.keySet()) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
                Player p = Bukkit.getPlayer(uuid);
                if(kp.isInArena()){
                    p.setAllowFlight(false);
                    p.setFlying(false);
                }
                p.setFlySpeed(0.1f);
            }
        }
        setActive(false);
        this.participants.clear();
        this.flyCooldown.clear();
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
