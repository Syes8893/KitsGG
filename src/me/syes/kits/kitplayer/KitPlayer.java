package me.syes.kits.kitplayer;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import me.syes.kits.Kits;
import me.syes.kits.bukkitevents.ExpChangeEvent;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.ConfigUtils;

public class KitPlayer {

	private UUID uuid;
	private boolean inArena;
	private boolean kitSelected;
	
	private int kills, killstreak, deaths;
	private int highestkillstreak;
	private int arrowsshot, arrowshit;
	private int potionsdrunk, potionsthrown;
	private double heartshealed;
	private int eventsPlayed, eventsWon;
	private HashSet<String> savedKits;
	private HashSet<String> unlockedKits;
	
	private HashSet<Entity> mobs;
	
	private Kit selectedKit;
	
	public KitPlayer(UUID uuid) {
		this.uuid = uuid;
		Kits.getInstance().playerManager.addKitPlayers(this);
		this.savedKits = new HashSet<String>();
		this.unlockedKits = new HashSet<String>();
		this.mobs = new HashSet<Entity>();
	}
	
	public KitPlayer(UUID uuid, FileConfiguration fc) {
		this.uuid = uuid;
		this.savedKits = new HashSet<String>();
		this.unlockedKits = new HashSet<String>();
		this.mobs = new HashSet<Entity>();
		this.loadStats(fc);
		Kits.getInstance().playerManager.addKitPlayers(this);
	}

	private void loadStats(FileConfiguration fc) {
		this.inArena = fc.getBoolean("inArena");
		this.kills = fc.getInt("Kills");
		this.killstreak = fc.getInt("Killstreak");
		this.highestkillstreak = fc.getInt("Highestkillstreak");
		this.deaths = fc.getInt("Deaths");
		this.arrowsshot = fc.getInt("Arrowsshot");
		this.arrowshit = fc.getInt("Arrowshit");
		this.potionsdrunk = fc.getInt("Potionsdrunk");
		this.potionsthrown = fc.getInt("Potionsthrown");
		this.heartshealed = fc.getInt("Heartshealed");
		this.eventsPlayed = fc.getInt("Eventsplayed");
		this.eventsWon = fc.getInt("Eventswon");
		List<String> savedKitsList = fc.getStringList("Savedkits");
		for(String str : savedKitsList)
			this.savedKits.add(str);
		List<String> unlockedKitsList = fc.getStringList("Unlockedkits");
		for(String str : unlockedKitsList)
			this.unlockedKits.add(str);
	}

	public String getName() {
		return Bukkit.getOfflinePlayer(this.uuid).getName();
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public boolean isInArena() {
		return inArena;
	}

	public void setInArena(boolean inArena) {
		this.inArena = inArena;
	}

	public boolean hasKitSelected() {
		return kitSelected;
	}

	public void setKitSelected(boolean kitSelected) {
		this.kitSelected = kitSelected;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void addKill() {
		Bukkit.getServer().getPluginManager().callEvent(new ExpChangeEvent(this, 1));
		this.kills += 1;
		this.killstreak += 1;
		if(this.killstreak > this.highestkillstreak)
			this.highestkillstreak = this.killstreak;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public void resetKillstreak() {
		this.killstreak = 0;
	}

	public int getHighestKillstreak() {
		return highestkillstreak;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void addDeath() {
		if((deaths+1) % 7 == 0)
			Bukkit.getServer().getPluginManager().callEvent(new ExpChangeEvent(this, 1));
		this.deaths += 1;
	}

	public double getKDR() {
		if(this.deaths > 0)
			return (double) this.kills / (double) this.deaths;
		else return (double) this.kills;
	}

	public int getArrowsShot() {
		return this.arrowsshot;
	}

	public void addArrowsShot() {
		this.arrowsshot += 1;
	}

	public int getArrowsHit() {
		return this.arrowshit;
	}

	public void addArrowsHit() {
		this.arrowshit += 1;
	}

	public int getPotionsDrunk() {
		return this.potionsdrunk;
	}

	public void addPotionsDrunk() {
		this.potionsdrunk += 1;
	}

	public int getPotionsThrown() {
		return this.potionsthrown;
	}

	public void addPotionsThrown() {
		this.potionsthrown += 1;
	}

	public double getHeartsHealed() {
		return this.heartshealed;
	}

	public void addHeartsHealed(double amt) {
		this.heartshealed += amt;
	}

	public int getEventsPlayed() {
		return this.eventsPlayed;
	}

	public void setEventsPlayed(int eventsPlayed) {
		this.eventsPlayed = eventsPlayed;
	}

	public void addEventsPlayed() {
		Bukkit.getServer().getPluginManager().callEvent(new ExpChangeEvent(this, 4));
		this.eventsPlayed += 1;
	}

	public int getEventsWon() {
		return this.eventsWon;
	}

	public void setEventsWon(int eventsWon) {
		this.eventsWon = eventsWon;
	}

	public void addEventsWon() {
		Bukkit.getServer().getPluginManager().callEvent(new ExpChangeEvent(this, 15));
		this.eventsWon += 1;
	}

	public Kit getSelectedKit() {
		return selectedKit;
	}

	public void setSelectedKit(Kit selectedKit) {
		this.selectedKit = selectedKit;
	}

	public void resetSelectedKit() {
		this.selectedKit = null;
	}

	public HashSet<Entity> getMobs() {
		return this.mobs;
	}

	public void addMob(Entity e) {
		this.mobs.add(e);
	}

	public void removeMobs() {
		for(Entity e : this.mobs)
			e.remove();
		this.mobs.clear();
	}
	
	public HashSet<String> getSavedKits() {
		return savedKits;
	}

	public void addSavedKit(String str) {
		this.savedKits.add(str);
	}

	public void removeSavedKit(String str) {
		this.savedKits.remove(str);
	}
	
	public HashSet<String> getUnlockedKits() {
		return unlockedKits;
	}

	public void addUnlockedKit(String str) {
		this.unlockedKits.add(str);
	}

	public void removeUnlockedKit(String str) {
		this.unlockedKits.remove(str);
	}

	//TODO - Exp System
	public int getExp() {
		return this.kills + this.eventsWon * 15 + this.eventsPlayed * 4 + this.deaths/7;
	}
	
}
