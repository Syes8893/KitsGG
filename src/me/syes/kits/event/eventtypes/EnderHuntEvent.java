package me.syes.kits.event.eventtypes;

import me.syes.kits.Kits;
import me.syes.kits.arena.Arena;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kit.Kit;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class EnderHuntEvent extends Event {
    private final ArrayList<Entity> endermites = new ArrayList<>();
    private Enderman enderman;
    private final Arena a = Kits.getInstance().getArenaManager().getArena();
    public ArrayList<Entity> getEndermites() {
        return endermites;
    }
    public EnderHuntEvent(EventManager eventManager) {
        this.eventManager = eventManager;
        this.participants = new HashMap<UUID, Double>();
        this.name = "EnderHunt";
        this.goal = "Kill as many Endermen or Endermites as possible.";
        this.rules = "No rules apply for this event.";

    }
    @Override
    public void startEvent() {
        this.announceEventStart();
        this.loadParticipants();
        this.setActive(true);
        this.time = this.durationSeconds;
        int maxEndermiteCount = 20 + 20 * ((Bukkit.getServer().getOnlinePlayers().size()) / 3);
        spawnEnderman();
        spawnEndermite(maxEndermiteCount);
        new BukkitRunnable() {
            public void run() {
                for (Entity endermite : endermites) {
                    if (endermite.getFireTicks() != 0) {
                        endermite.setFireTicks(0);
                    }
                    if (enderman.getFireTicks() != 0) {
                        enderman.setFireTicks(0);
                    }
                    if (endermite.isOnGround()) {
                        Random random = new Random();
                        int r = random.nextInt(3);
                        if (r == 0) {
                            jumpEdermite(endermite);
                        }
                    }
                    if (endermite.isDead()) {
                        endermites.remove(endermite);
                        spawnEndermite(1);
                    }
                }
                Random random = new Random();
                teleportEndermite(endermites, (maxEndermiteCount - random.nextInt(maxEndermiteCount)));
                if (endermites.size() < maxEndermiteCount) {
                    spawnEndermite(maxEndermiteCount - endermites.size());
                }
                if (time % 10 == 0) {
                    teleportEnderman(enderman);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        int r = random.nextInt(6);
                        if (Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId()).isInArena()) {
                            if (r == 0) {
                                blindnessAbility(p);
                            }
                            if (r == 1) {
                                teleportAbility(p);
                            }
                        }
                    }
                }
                if (time == 0) {
                    finishEvent();
                    this.cancel();
                    time++;
                } else if (!active) {
                    this.cancel();
                    time++;
                }
                time--;
            }
        }.runTaskTimer(Kits.getInstance(), 0, 20);
    }

    public void spawnEnderman() {
        enderman = (Enderman) a.getWorld().spawnEntity(a.getRandomSpawn(), EntityType.ENDERMAN);
        enderman.setMaxHealth(100000);
        enderman.setHealth(2048);
        enderman.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, true));
    }
    private void teleportEnderman(Entity e) {
        e.teleport(a.getRandomSpawn());
    }
    public void spawnEndermite(int amount) {
        for (int i = 0; i < amount; i++) {
            Endermite endermite = (Endermite) a.getWorld().spawnEntity(a.getRandomSpawn(), EntityType.ENDERMITE);
            endermite.setMaxHealth(100000);
            endermite.setHealth(2048);
            endermite.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, true));
            endermite.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, true));
            endermites.add(endermite);
        }
    }
    private void teleportEndermite(ArrayList<Entity> array, int n) {
        array.get(n).teleport(a.getRandomSpawn());
    }
    private void jumpEdermite(Entity e) {
        e.setVelocity(new Vector(0, 0.5, 0));
    }
    private void blindnessAbility(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 100, true));
        p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1f, 1f);
    }
    private void teleportAbility(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 100, true));
        p.teleport(a.getRandomSpawn());
        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
    }
    @Override
    public void finishEvent() {
        this.announceEventEnd();
        setActive(false);
        this.participants.clear();
        enderman.remove();
        for(Entity e : endermites) {
            e.remove();
        }
        time = durationSeconds;
        resetArena();
    }
}
