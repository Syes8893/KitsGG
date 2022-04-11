package me.syes.kits.event;

import java.text.DecimalFormat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
		if(eventManager.getActiveEvent() != null) {
			if(eventManager.getActiveEvent().getParticipants().contains(kp));
				eventManager.getActiveEvent().addParticipant(kp);
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
		if(eventManager.getMarksmanEvent().isActive())
			if(e.getEntity() instanceof Player)
				if(e.getDamager() instanceof Arrow)
					if(((Arrow)e.getDamager()).getShooter() instanceof Player) {
						int distance = (int) ((Player)((Arrow)e.getDamager()).getShooter()).getLocation().distance(e.getEntity().getLocation());
						KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(((Player)((Arrow)e.getDamager()).getShooter()).getUniqueId());
						eventManager.getMarksmanEvent().addParticipantScore(kp);
						ActionBarMessage.sendMessage((Player)((Arrow)e.getDamager()).getShooter(), "§d+1 Score §7(Landed Bowshot)");
						if(distance > eventManager.getMarksmanEvent().getInstakillBlocks()) {
							e.setDamage(((Player)e.getEntity()).getHealth());
						}
					}
		if(eventManager.getBossEvent().isActive()) {
			if(e.getEntity() == eventManager.getBossEvent().getBoss())
				if(e.getDamager() instanceof Player) {
					KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(((Player)e.getDamager()).getUniqueId());
					eventManager.getBossEvent().addParticipantSpecifiedScore(kp, e.getFinalDamage());
					ActionBarMessage.sendMessage((Player)e.getDamager(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Boss)");
				}
				else if(e.getDamager() instanceof Projectile)
					if(((Projectile)e.getDamager()).getShooter() instanceof Player) {
						KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(((Player)((Projectile)e.getDamager()).getShooter()).getUniqueId());
						eventManager.getBossEvent().addParticipantSpecifiedScore(kp, e.getFinalDamage());
						ActionBarMessage.sendMessage((Player)((Projectile)e.getDamager()).getShooter(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Boss)");
					}
			if(e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile))
				e.setDamage(0);
		}
		else if(eventManager.getShowdownEvent().isActive()) {
			if(e.getEntity() instanceof Player)
				if(e.getDamager() instanceof Player && e.getDamager() != e.getEntity()) {
					KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(((Player)e.getDamager()).getUniqueId());
					eventManager.getShowdownEvent().addParticipantSpecifiedScore(kp, (int) e.getFinalDamage());
					ActionBarMessage.sendMessage((Player)e.getDamager(), "§d+" + new DecimalFormat("#.#").format(e.getFinalDamage()) + " Score §7(Damaged Player)");
				}
				else if(e.getDamager() instanceof Projectile)
					if(((Projectile)e.getDamager()).getShooter() instanceof Player && ((Projectile)e.getDamager()).getShooter() != e.getEntity()) {
						KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(((Player)((Projectile)e.getDamager()).getShooter()).getUniqueId());
						eventManager.getShowdownEvent().addParticipantSpecifiedScore(kp, (int) e.getFinalDamage());
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
				KitPlayer kpKiller = Kits.getInstance().getPlayerManager().getKitPlayers().get(killer.getUniqueId());
				eventManager.getRamboEvent().addParticipantScore(kpKiller);
				killer.setHealth(killer.getMaxHealth());
				ActionBarMessage.sendMessage(killer, "§c+10\u2764 §7(Kill, Rambo Event)");
			}
		}
		if(eventManager.getMarksmanEvent().isActive()) {
			if(e.getEntity().getKiller() != null && e.getEntity().getKiller() != e.getEntity()) {
				//Player killer = e.getEntity().getKiller();
				//KitPlayer kpKiller = Kits.getInstance().getPlayerManager().getKitPlayers().get(killer.getUniqueId());
				for(ItemStack is : eventManager.getMarksmanEvent().getDropsOnDeath())
					e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), is);
			}
		}
		if(eventManager.getShowdownEvent().isActive())
			e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
		if(eventManager.getGoldRushEvent().isActive()) {
			for(ItemStack i : e.getDrops())
				if(i.getType().equals(Material.GOLD_NUGGET))
					e.getDrops().remove(i);
			e.getDrops().add(new ItemStack(Material.GOLD_NUGGET, 1 + kp.getKillstreak()));
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(e.getPlayer().getUniqueId());
		if(eventManager.getGoldRushEvent().isActive())
			if(e.getItem().getItemStack().getType().equals(Material.GOLD_NUGGET)) {
				e.setCancelled(true);
				e.getItem().remove();
				eventManager.getGoldRushEvent().addParticipantSpecifiedScore(kp, e.getItem().getItemStack().getAmount());
				ActionBarMessage.sendMessage(p, "§d+" + e.getItem().getItemStack().getAmount() + " Score §7(Picked up Gold)");
				p.playSound(p.getLocation(),Sound.NOTE_PLING, 1.0F, 100.0F);
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
