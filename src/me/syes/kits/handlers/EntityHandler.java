package me.syes.kits.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityHandler implements Listener{
	
	@EventHandler
	public void onEntityTargetPlayer(EntityTargetEvent e) {
		if(!(e.getTarget() instanceof Player))
			return;
		Player p = (Player) e.getTarget();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(kp.getMobs().contains(e.getEntity())) {
			e.setCancelled(true);
			for(Entity t : e.getEntity().getNearbyEntities(25, 25, 25))
				if(t instanceof Player) {
					e.setTarget(t);
					return;
				}
		}
	}
	
	@EventHandler
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Player))
			return;;
		Player p = (Player) e.getEntity();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(kp.getMobs().contains(e.getDamager()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySplit(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof Slime))
			return;
		e.getEntity().remove();
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		if(!(e.getEntity() instanceof Slime))
			return;
		((Slime) e.getEntity()).setSize(3);
	}

	@EventHandler
	public void onItemDrop(ItemSpawnEvent e){
		if(e.getEntity().getItemStack().getType() != Material.GOLD_NUGGET
				&& Kits.getInstance().getEventManager().getActiveEvent() == Kits.getInstance().getEventManager().getGoldRushEvent())
			new BukkitRunnable(){
				@Override
				public void run() {
					if(!e.getEntity().isDead())
						e.getEntity().remove();
				}
			}.runTaskLater(Kits.getInstance(), 15*20);
	}

}
