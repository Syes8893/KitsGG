package me.syes.kits.event;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.syes.kits.Kits;
import me.syes.kits.arena.Arena;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;
import me.syes.kits.utils.MessageUtils;

public abstract class Event {

	protected EventManager eventManager;
	
	protected HashMap<KitPlayer, Double> participants;
	protected String name;
	protected String goal;
	protected String rules;
	protected int durationSeconds = ConfigUtils.getConfigSection("Event").getInt("Duration-Seconds");
	
	protected int requiredPlayers = ConfigUtils.getConfigSection("Event").getInt("Required-Players");
	protected boolean active;
	
	protected int time;
	
	public abstract void startEvent();
	
	public abstract void finishEvent();
	
	public void onArenaEnter(Player p) {
		//Do nothing
	}
	
	public void announceEventStart() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.announceEventStart(p);
		}
	}
	
	public void announceEventStart(Player p) {
		MessageUtils.sendTitle(p, "&d&l" + this.name.toUpperCase(), this.goal);
		p.sendMessage("�7�m--------------------------------------------------");
		p.sendMessage("�d�l" + this.name.toUpperCase() + " EVENT HAS STARTED " + " \n�fObjective: �a" + this.goal + "\n�7�o" + this.rules);
		p.sendMessage("�7�m--------------------------------------------------");
	}
	
	public void announceEventEnd() {
		MessageUtils.broadcastTitle("&d&lEVENT ENDED", "&fWinner: " + eventManager.getEventTop().get(1).getName());
		MessageUtils.broadcastMessage("&7&m------------------------------");
		MessageUtils.broadcastMessage("&d&lEVENT HAS ENDED");
		for(int i = 1; i < 4; i ++) {
			if(i > eventManager.getEventTop().size()) break;
			MessageUtils.broadcastMessage("&7#" + i + " " + Kits.getInstance().getExpManager().getLevel(eventManager.getEventTop().get(i).getExp()).getPrefix()
				+ eventManager.getEventTop().get(i).getName());
		}
		MessageUtils.broadcastMessage("&7&m------------------------------");
		eventManager.getEventTop().get(1).addEventsWon();
	}
	
	public void loadParticipants() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
			participants.put(kp, 0.0);
			kp.addEventsPlayed();
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
				for(Entity e : kp.getMobs())
					e.remove();
			}
		}
		Arena a = Kits.getInstance().getArenaManager().getArena();
		for(Entity e : a.getCenter().getWorld().getNearbyEntities(a.getCenter()
				, Math.abs(a.getCenter().getBlockX() - a.getMinBounds().getBlockX()), 256
				, Math.abs(a.getCenter().getBlockZ() - a.getMinBounds().getBlockZ()))) {
			if(e.getType() == EntityType.DROPPED_ITEM
					|| e.getType() == EntityType.ARROW)
				e.remove();
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Set<KitPlayer> getParticipants() {
		return participants.keySet();
	}
	
	public void addParticipant(KitPlayer kp) {
		participants.put(kp, 0.0);
	}
	
	public void setParticipantScore(KitPlayer kp, double score) {
		participants.put(kp, score);
	}
	
	public void addParticipantScore(KitPlayer kp) {
		participants.put(kp, participants.get(kp)+1);
	}
	
	public void addParticipantSpecifiedScore(KitPlayer kp, double score) {
		participants.put(kp, participants.get(kp) + score);
	}
	
	public double getParticipantScore(KitPlayer kp) {
		return participants.get(kp);
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
