package me.syes.kits.event;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.syes.kits.Kits;
import me.syes.kits.arena.Arena;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;
import me.syes.kits.utils.MessageUtils;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Event {

	protected EventManager eventManager;
	
	protected HashMap<UUID, Double> participants;
	protected String name;
	protected String goal;
	protected String rules;
	protected int durationSeconds = ConfigUtils.getConfigSection("Event").getInt("Duration-Seconds");
	
	protected int requiredPlayers = ConfigUtils.getConfigSection("Event").getInt("Required-Players");
	protected boolean active;
	
	protected int time;
	protected int totalScore;

	public abstract void startEvent();
	
	public abstract void finishEvent();
	
	public void onArenaEnter(Player p) {
		if(!participants.keySet().contains(p.getUniqueId()))
			addParticipant(p.getUniqueId());
	}
	
	public void announceEventStart() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.announceEventStart(p);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
		}
	}
	
	public void announceEventStart(Player p) {
		MessageUtils.sendTitle(p, "&d&l" + this.name.toUpperCase(), this.goal);
		p.sendMessage("§7§m--------------------------------------------------");
		p.sendMessage("§d§l" + this.name.toUpperCase() + " EVENT HAS STARTED " + " \n§fObjective: §a" + this.goal + "\n§7§o" + this.rules);
		p.sendMessage("§7§m--------------------------------------------------");
	}
	
	public void announceEventEnd() {
		if(eventManager.getEventTop().size() > 0)
			MessageUtils.broadcastTitle("&d&lEVENT ENDED", "&fWinner: " + Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(1)).getName());
		MessageUtils.broadcastMessage("&7&m------------------------------");
		MessageUtils.broadcastMessage("&d&lEVENT HAS ENDED");
		for (int i = 1; i < 4; i++) {
			if (i > eventManager.getEventTop().size()) break;
			MessageUtils.broadcastMessage("&7#" + i + " "
					+ Kits.getInstance().getExpManager().getLevel(Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(i)).getExp()).getPrefix()
					+ Kits.getInstance().getExpManager().getLevel(Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(i)).getExp()).getNameColor()
					+ Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(i)).getName() + ": &f" + participants.get(eventManager.getEventTop().get(i)));
		}
		MessageUtils.broadcastMessage("&7&m------------------------------");
		this.giveExp();
		for (UUID uuid : participants.keySet()) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(uuid);
			kp.addEventsPlayed();
		}
		if(eventManager.getEventTop().size() > 0)
			Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(1)).addEventsWon();
	}

	public void giveExp(){
		double totalScore = 0;
		for(double score : participants.values()){
			totalScore += score;
		}
		int expPool = participants.keySet().size() * 25;
		for(UUID uuid : participants.keySet()){
			int expPart = (int) ((participants.get(uuid)/totalScore) * expPool);
			if(Bukkit.getOfflinePlayer(uuid).isOnline())
				Bukkit.getPlayer(uuid).sendMessage("§7§oYou received " + expPart + " EXP as a participation bonus!");
			Kits.getInstance().getPlayerManager().getKitPlayer(uuid).addEventExp(expPart);
		}
	}
	
	public void loadParticipants() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
			if(kp.isInArena())
				participants.put(p.getUniqueId(), 0.0);
		}
	}
	
	public void resetArena() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setMaxHealth(20);
			p.setHealth(p.getMaxHealth());
			for(PotionEffect pe : p.getActivePotionEffects())
				p.removePotionEffect(pe.getType());
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
			if(kp.isInArena()) {
				kp.setInArena(false);
				kp.setSelectedKit(null);
				kp.setKitSelected(false);
				p.teleport(Kits.getInstance().getArenaManager().getArena().getLobbySpawn());
				p.setSaturation(5);
				p.setFoodLevel(20);
				p.setHealth(20);
				p.setFireTicks(0);
				for(Entity e : kp.getMobs())
					e.remove();
			}
		}
		Arena a = Kits.getInstance().getArenaManager().getArena();
		new BukkitRunnable(){
			@Override
			public void run() {
				for(Entity e : a.getCenter().getWorld().getNearbyEntities(a.getCenter()
						, Math.abs(a.getCenter().getBlockX() - a.getMinBounds().getBlockX()), 256
						, Math.abs(a.getCenter().getBlockZ() - a.getMinBounds().getBlockZ()))) {
					if(e.getType() == EntityType.DROPPED_ITEM
							|| e.getType() == EntityType.ARROW)
						e.remove();
				}
			}
		}.runTask(Kits.getInstance());
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Set<UUID> getParticipants() {
		return participants.keySet();
	}
	
	public void addParticipant(UUID uuid) {
		participants.put(uuid, 0.0);
	}
	
	public void setParticipantScore(UUID uuid, double score) {
		participants.put(uuid, score);
	}
	
	public void addParticipantScore(UUID uuid) {
		participants.put(uuid, participants.get(uuid)+1);
	}
	
	public void addParticipantSpecifiedScore(UUID uuid, double score) {
		participants.put(uuid, participants.get(uuid) + score);
	}
	
	public double getParticipantScore(UUID uuid) {
		return participants.get(uuid);
	}

	public String getName() {
		return name;
	}

	public String getGoal() {
		return goal;
	}

	public String getRules() {
		return rules;
	}

	public int getRequiredPlayers() {
		return requiredPlayers;
	}

	public String getTimeLeft() {
    	if(time > 60)
    		return time/60 + 1 + "m";
    	else if(time <= 60)
    		return time + "s";
    	return "ERROR";
	}

	public int getTimeLeftInteger() {
    	return time;
	}

}
