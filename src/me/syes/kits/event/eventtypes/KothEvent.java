package me.syes.kits.event.eventtypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ActionBarMessage;
import me.syes.kits.utils.ConfigUtils;
import me.syes.kits.utils.ItemUtils;

public class KothEvent extends Event {

	private List<Material> platformMaterials;
	private double damageMultiplier;
	
	public KothEvent(EventManager eventManager) {
		platformMaterials = new ArrayList<Material>();
		for(String str : ConfigUtils.getConfigSection("Event.KoTH").getStringList("Platform-Materials")) {
			if(Material.getMaterial(str.replace(" ", "_").toUpperCase()) == null)
				System.out.println("[ERROR] Unknown material for entry: " + str + " (At Event: KoTH: Platform-Materials: " + str + ")");
			this.platformMaterials.add(Material.getMaterial(str.replace(" ", "_").toUpperCase()));
		}
		
		this.eventManager = eventManager;
		this.participants = new HashMap<KitPlayer, Double>();
		this.name = "KoTH";
		this.goal = "Stand in the KoTH area for as long as possible to win.";
		this.damageMultiplier = ConfigUtils.getConfigSection("Event.KoTH").getDouble("Damage-Multiplier");
		this.rules = "All players receive a knockback 2 stick. All damage is reduced by " + (100 - (damageMultiplier * 100)) + "%.";
	}
	
	@Override
	public void startEvent() {
		this.announceEventStart();
		this.loadParticipants();
		for(KitPlayer kp : this.getParticipants()) {
			if(kp.isInArena())
				if(Bukkit.getOfflinePlayer(kp.getUuid()).isOnline()) {
					Player p = Bukkit.getPlayer(kp.getUuid());
					onArenaEnter(p);
				}
			
		}
		this.setActive(true);
		this.time = this.durationSeconds;
		new BukkitRunnable() {
			public void run() {
				checkLocation();
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
	
	public void checkLocation() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
			if(participants.containsKey(kp)) {
				int x = p.getLocation().getBlockX();
				int z = p.getLocation().getBlockZ();
				int y = p.getLocation().getBlockY();
				while(p.getWorld().getBlockAt(x, y, z).getType().equals(Material.AIR)){
					y += -1;
				}
				if(this.platformMaterials.contains(p.getWorld().getBlockAt(new Location(p.getWorld(), x, y, z)).getType())) {
					this.addParticipantScore(kp);
					ActionBarMessage.sendMessage(p, "§d+1 Score §7(Contested Platform)");
				}
			}
		}
	}

	@Override
	public void onArenaEnter(Player p) {
		HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		enchants.put(Enchantment.KNOCKBACK, 2);
		ItemStack is = ItemUtils.buildEnchantedItem(new ItemStack(Material.STICK)
				, "&eKnockback Stick", Arrays.asList("§7Obtained during the KoTH event."), enchants);
		p.getInventory().addItem(is);
	}
	
	public double getDamageMultiplier() {
		return this.damageMultiplier;
	}

}
