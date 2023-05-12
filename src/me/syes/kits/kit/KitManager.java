package me.syes.kits.kit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class KitManager {
	
	public ArrayList<Kit> kits;

	public KitManager() {
		kits = new ArrayList<Kit>();
	}
	
	public Kit getKit(String name, int level) {
		for(Kit k : kits)
			if(k.getName().equalsIgnoreCase(name) && k.getLevel() == level)
				return k;
		return null;
	}

	public Kit getKit(String name){
		for(Kit k : kits)
			if(k.getName().equalsIgnoreCase(name))
				return k;
		return null;
	}

//	public Kit getNextPrestigeKit(String name, int level){
//		for(Kit k : kits)
//			if(k.getName().equals(name) && k.getPrestigeLevel() == level+1)
//				return k;
//		return null;
//	}
	
	public void giveKit(Player p, Kit k) {
		if(isAllowedKit(p, k, true)) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
			while(k.hasUpgrade()){
				if(kp.getExp() >= Kits.getInstance().getKitManager().getKit(k.getName(), k.getLevel()+1).getRequiredExp())
					k = Kits.getInstance().getKitManager().getKit(k.getName(), k.getLevel()+1);
				else
					break;
			}
//			if(k.hasUpgrade()){
//				Kit pres = Kits.getInstance().getKitManager().getKit(k.getName() + "_prestige");
//				if(kp.getExp() >= pres.getRequiredExp()){
//					giveKit(p, pres);
//					return;
//				}
//			}
			k.giveKit(p);
			p.sendMessage("§aYou have received the " + k.getName() + " Kit.");
		}
	}
	
	public boolean isAllowedKit(Player p, Kit k, boolean sendMessages) {
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(ConfigUtils.perKitPermissions && !p.hasPermission("kits." + k.getName().toLowerCase()) ) {
			if(sendMessages) p.sendMessage("§cYou don't have permission to use this kit.");
			return false;
		}
		else if(kp.getExp() < k.getRequiredExp()) {
			if(sendMessages) p.sendMessage("§cYou don't have enough Exp to use this kit. (" + kp.getExp() + "/" + k.getRequiredExp() + ")");
			return false;
		}
		return true;
	}
	
	public void giveRandomKit(Player p) {
		Kit k = kits.get(new Random().nextInt(kits.size()));
		while(!p.hasPermission("kits." + k.getName().toLowerCase()) && ConfigUtils.getConfigSection("Kits").getBoolean("Per-Kit-Permission")) {
			k = kits.get(new Random().nextInt(kits.size()));
		}
		while(Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId()).getExp() < k.getRequiredExp()) {
			k = kits.get(new Random().nextInt(kits.size()));
		}
		k.giveKit(p);
		p.sendMessage("§aYou have received the " + k.getName() + " Kit. §7(Random Kit)");
	}
	
	public Kit getRandomKit() {
		return kits.get(new Random().nextInt(kits.size()));
	}
	
	public void organiseKits() {
    	Comparator<Kit> sorter = new Comparator<Kit>() {
			@Override
			public int compare(Kit a, Kit b) {
				if(a.getName().charAt(0) > b.getName().charAt(0)) return 1;
				if(a.getName().charAt(0) == b.getName().charAt(0)) return 0;
				return -1;
			}
    	};
    	this.kits.sort(sorter);
	}

	public ArrayList<Kit> getKits() {
		return kits;
	}

	public void addKit(Kit kit) {
		kits.add(kit);
	}

	public void removeKit(Kit kit) {
		kits.remove(kit);
	}

}
