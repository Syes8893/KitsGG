package me.syes.kits.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class StatsHandler implements Listener {
	
	//Handles Kill + Death stats
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if(!ConfigUtils.getConfigSection("Event").getBoolean("Track-Stats-During-Event") && Kits.getInstance().getEventManager().getActiveEvent() != null)
			return;
		Player p = e.getEntity();
		KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(kitPlayer.isInArena()) {
			kitPlayer.addDeath();
			kitPlayer.resetKillstreak();
		}
		Player k = e.getEntity().getKiller();
		if(k != null && p != k) {
			KitPlayer killerKitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(k.getUniqueId());
			if(killerKitPlayer.isInArena()) killerKitPlayer.addKill();
		}
	}
	
	@EventHandler
	public void onPlayerDeathByWolf(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player)
			if(e.getFinalDamage() > ((Player)e.getEntity()).getHealth())
				if(e.getDamager() instanceof Wolf) {
					Player killer = (Player) ((Wolf)e.getDamager()).getOwner();
					KitPlayer killerKitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(killer.getUniqueId());
					killerKitPlayer.addKill();
				}
	}
	
	@EventHandler
	public void onShootBow(EntityShootBowEvent e) {
		if(!ConfigUtils.getConfigSection("Event").getBoolean("Track-Stats-During-Event") && Kits.getInstance().getEventManager().getActiveEvent() != null)
			return;
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
			if(kitPlayer.isInArena() && !e.isCancelled()) kitPlayer.addArrowsShot();
		}
	}
    
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
		if(!ConfigUtils.getConfigSection("Event").getBoolean("Track-Stats-During-Event") && Kits.getInstance().getEventManager().getActiveEvent() != null)
			return;
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            if (((Arrow)e.getDamager()).getShooter() instanceof Player) {
                Player damager = (Player)((Arrow)e.getDamager()).getShooter();
    			KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(damager.getUniqueId());
    			if(kitPlayer.isInArena() && !e.isCancelled()) kitPlayer.addArrowsHit();
            }
        }
    }
    
    @EventHandler
    public void onDrinkPotion(PlayerItemConsumeEvent e) {
		if(!ConfigUtils.getConfigSection("Event").getBoolean("Track-Stats-During-Event") && Kits.getInstance().getEventManager().getActiveEvent() != null)
			return;
    	if(e.getItem() != null) {
    		ItemStack item = e.getItem();
    		if(item.getType() == Material.POTION) {
        		Player p = e.getPlayer();
    			KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
    			if(kitPlayer.isInArena() && !e.isCancelled()) kitPlayer.addPotionsDrunk();
    		}
    	}
    }
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
		if(!ConfigUtils.getConfigSection("Event").getBoolean("Track-Stats-During-Event") && Kits.getInstance().getEventManager().getActiveEvent() != null)
			return;
        if(e.getEntity().getShooter() instanceof Player) {
        	Player p = (Player) e.getEntity().getShooter();
			KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
			if(kitPlayer.isInArena() && !e.isCancelled()) kitPlayer.addPotionsThrown();
        }
    }
    
    @EventHandler
    public void onHeartRegain(EntityRegainHealthEvent e) {
		if(!ConfigUtils.getConfigSection("Event").getBoolean("Track-Stats-During-Event") && Kits.getInstance().getEventManager().getActiveEvent() != null)
			return;
    	if(e.getEntity() instanceof Player && e.getRegainReason() != RegainReason.SATIATED) {
			KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(((Player)e.getEntity()).getUniqueId());
			kitPlayer.addHeartsHealed(e.getAmount()/2);
    	}
    }
    
    @EventHandler
    public void onArenaLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(kp.isInArena()) {
			kp.resetKillstreak();
			kp.addDeath();
			if(p.getKiller() != null) {
				Player killer = p.getKiller();
				KitPlayer killerKitPlayer = Kits.getInstance().getPlayerManager().getKitPlayer(killer.getUniqueId());
				if(killerKitPlayer.isInArena()) {
					killerKitPlayer.addKill();
				}
			}
		}
    }

}
