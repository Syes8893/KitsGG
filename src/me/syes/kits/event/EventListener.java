package me.syes.kits.event;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ActionBarMessage;

public class EventListener implements Listener {
	
	private EventManager eventManager;
	
	public EventListener() {
		eventManager = Kits.getInstance().getEventManager();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		//KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
		if(eventManager.getActiveEvent() != null) {
//			if(!eventManager.getActiveEvent().getParticipants().contains(p.getUniqueId()));
//				eventManager.getActiveEvent().addParticipant(p.getUniqueId());
			eventManager.getActiveEvent().announceEventStart(p);
			if(eventManager.getShowdownEvent().isActive())
				eventManager.getShowdownEvent().setDoubleHealth(p);
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(eventManager.getRamboEvent().isActive())
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if(p.getHealth() - e.getDamage() > 0) {
					p.setHealth(p.getHealth() - e.getDamage());
					e.setDamage(0);
				}else if(p.getHealth() - e.getDamage() < 0) {
					p.setHealth(1);
					e.setDamage(100);
				}
			}
		if(eventManager.getKothEvent().isActive())
			e.setDamage(e.getDamage() * eventManager.getKothEvent().getDamageMultiplier());
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(eventManager.getMarksmanEvent().isActive()){
			if(e.getEntity() instanceof Player)
				if(e.getDamager() instanceof Arrow)
					if(((Arrow)e.getDamager()).getShooter() instanceof Player) {
						int distance = (int) ((Player)((Arrow)e.getDamager()).getShooter()).getLocation().distance(e.getEntity().getLocation());
						UUID uuid = ((Player)((Arrow)e.getDamager()).getShooter()).getUniqueId();
						int scoreGained = (int) Math.max(Math.pow((double)distance/10.0, 2), 1);
						eventManager.getMarksmanEvent().setParticipantScore(uuid, eventManager.getMarksmanEvent().getParticipantScore(uuid) + scoreGained);
						ActionBarMessage.sendMessage((Player)((Arrow)e.getDamager()).getShooter(), "§d+" + scoreGained + " Score §7(Landed Bowshot)");
					}
		}
		else if(eventManager.getPaintballEvent().isActive()) {
			if (e.getEntity() instanceof Player)
				if (e.getDamager() instanceof Snowball)
					if (((Snowball) e.getDamager()).getShooter() instanceof Player) {
						UUID uuid = ((Player) ((Snowball) e.getDamager()).getShooter()).getUniqueId();
						eventManager.getPaintballEvent().setParticipantScore(uuid, eventManager.getPaintballEvent().getParticipantScore(uuid) + 1);
						e.setDamage(EntityDamageEvent.DamageModifier.BASE, 3);
						ActionBarMessage.sendMessage((Player) ((Snowball) e.getDamager()).getShooter(), "§d+1" + " Score §7(Hit Player)");
					}
		}
		else if(eventManager.getBossEvent().isActive()) {
			if(e.getEntity() == eventManager.getBossEvent().getBoss())
				if(e.getDamager() instanceof Player) {
					UUID uuid = ((Player)e.getDamager()).getUniqueId();
					eventManager.getBossEvent().addParticipantSpecifiedScore(uuid, e.getFinalDamage());
					ActionBarMessage.sendMessage((Player)e.getDamager(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Boss)");
				}
				else if(e.getDamager() instanceof Projectile)
					if(((Projectile)e.getDamager()).getShooter() instanceof Player) {
						UUID uuid = ((Player)((Projectile)e.getDamager()).getShooter()).getUniqueId();
						eventManager.getBossEvent().addParticipantSpecifiedScore(uuid, e.getFinalDamage());
						ActionBarMessage.sendMessage((Player)((Projectile)e.getDamager()).getShooter(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Boss)");
					}
			if(e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile))
				e.setDamage(0);
		}
		else if(eventManager.getShowdownEvent().isActive()) {
			if(e.getEntity() instanceof Player)
				if(e.getDamager() instanceof Player && e.getDamager() != e.getEntity()) {
					UUID uuid = ((Player)e.getDamager()).getUniqueId();
					eventManager.getShowdownEvent().addParticipantSpecifiedScore(uuid, (int) e.getFinalDamage());
					ActionBarMessage.sendMessage((Player)e.getDamager(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Player)");
				}
				else if(e.getDamager() instanceof Projectile)
					if(((Projectile)e.getDamager()).getShooter() instanceof Player && ((Projectile)e.getDamager()).getShooter() != e.getEntity()) {
						UUID uuid = ((Player)((Projectile)e.getDamager()).getShooter()).getUniqueId();
						eventManager.getShowdownEvent().addParticipantSpecifiedScore(uuid, (int) e.getFinalDamage());
						ActionBarMessage.sendMessage((Player)((Projectile)e.getDamager()).getShooter(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Player)");
					}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(e.getEntity().getUniqueId());
		if(eventManager.getRamboEvent().isActive()) {
			if(e.getEntity().getKiller() != null && e.getEntity().getKiller() != e.getEntity()) {
				Player killer = e.getEntity().getKiller();
				eventManager.getRamboEvent().addParticipantScore(killer.getUniqueId());
				killer.setHealth(killer.getMaxHealth());
				ActionBarMessage.sendMessage(killer, "§c+10\u2764 §7(Kill, Rambo Event)");
			}
		}
		else if(eventManager.getMarksmanEvent().isActive()) {
			if(e.getEntity().getKiller() != null && e.getEntity().getKiller() != e.getEntity()) {
				//Player killer = e.getEntity().getKiller();
				//KitPlayer kpKiller = Kits.getInstance().getPlayerManager().getKitPlayers().get(killer.getUniqueId());
				for(ItemStack is : eventManager.getMarksmanEvent().getDropsOnDeath())
					e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), is);
			}
		}
		else if(eventManager.getPaintballEvent().isActive()) {
			if(e.getEntity().getKiller() != null && e.getEntity().getKiller() != e.getEntity()) {
				if(e.getEntity().getLastDamageCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
					Player killer = e.getEntity().getKiller();
					eventManager.getPaintballEvent().setParticipantScore(killer.getUniqueId(), eventManager.getPaintballEvent().getParticipantScore(killer.getUniqueId()) + 3);
					ActionBarMessage.sendMessage(killer, "§d+3" + " Score §7(Hit Player)");
					//removing itemdrops doesnt work, perhaps next time?
//					Iterator it = e.getDrops().iterator();
//					while(it.hasNext())
//						if(((ItemStack)it.next()).getType().equals(Material.SNOW_BALL))
//							it.remove();
				}
			}
		}
		else if(eventManager.getShowdownEvent().isActive())
			e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
		else if(eventManager.getGoldRushEvent().isActive()) {
			for(ItemStack i : e.getDrops())
				if(i.getType().equals(Material.GOLD_NUGGET))
					e.getDrops().remove(i);
			e.getDrops().add(new ItemStack(Material.GOLD_NUGGET, 1 + kp.getKillstreak()));
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
//		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
		if(eventManager.getGoldRushEvent().isActive()) {
			if (e.getItem().getItemStack().getType().equals(Material.GOLD_NUGGET)) {
				e.setCancelled(true);
				e.getItem().remove();
				eventManager.getGoldRushEvent().addParticipantSpecifiedScore(p.getUniqueId(), e.getItem().getItemStack().getAmount());
				ActionBarMessage.sendMessage(p, "§d+" + e.getItem().getItemStack().getAmount() + " Score §7(Picked up Gold)");
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 100.0F);
			}
		}
		else if(eventManager.getPaintballEvent().isActive()) {
			if(e.getItem().getItemStack().getType().equals(Material.SNOW_BALL)) {
				e.setCancelled(true);
				e.getItem().remove();
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event){
		Player p = event.getPlayer();
//		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
		if(eventManager.getPaintballEvent().isActive()) {
			if(event.getItemDrop().getItemStack().getType().equals(Material.SNOW_BALL)){
				event.setCancelled(true);
				p.updateInventory();
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(eventManager.getBossEvent().isActive()) {
			if(!(e.getEntity() instanceof Player)) {
				e.setDroppedExp(0);
				e.getDrops().clear();
			}
		}
	}
	
	@EventHandler
	public void onEntityBurn(EntityCombustEvent e) {
		if(eventManager.getBossEvent().isActive())
			e.setCancelled(true);
	}
    
    @EventHandler
    public void onTrackerUse(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand() == null)
			return;
		if(!eventManager.getBossEvent().isActive())
			return;
		if(e.getPlayer().getItemInHand().getType() == Material.COMPASS) {
			e.getPlayer().setCompassTarget(eventManager.getBossEvent().getBoss().getLocation());
		ActionBarMessage.sendMessage(e.getPlayer(), "§fNow Tracking: §a" + eventManager.getBossEvent().getBoss().getCustomName()
		+ " §7(" + new DecimalFormat("#.#").format(e.getPlayer().getLocation().distance(eventManager.getBossEvent().getBoss().getLocation())) + " Blocks)");
		}
    }
	
}
