package me.syes.kits.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.gui.KitsGUI;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ActionBarMessage;
import me.syes.kits.utils.ConfigUtils;

public class PlayerHandler implements Listener {
	
	private boolean allowWorkbench;
	private boolean allowEnchtab;
	private boolean allowAnvil;

	private boolean itemDrops;
	private boolean lootchestOnBurn;
	
	public PlayerHandler() {
		this.allowWorkbench = ConfigUtils.getConfigSection("Arena.Utility").getBoolean("Allow-Crafting");
		this.allowEnchtab = ConfigUtils.getConfigSection("Arena.Utility").getBoolean("Allow-Enchanting");
		this.allowAnvil = ConfigUtils.getConfigSection("Arena.Utility").getBoolean("Allow-Anvil-Usage");

		this.itemDrops = ConfigUtils.getConfigSection("Arena.Player-Deaths").getBoolean("Item-Drops");
		this.lootchestOnBurn = ConfigUtils.getConfigSection("Arena.Player-Deaths").getBoolean("Loot-Chest-On-Burn");
	}
	
	@EventHandler
	public void onFarmlandTrample(PlayerInteractEvent e) {
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockGrow(BlockGrowEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(!p.hasPermission("kits.basic")) return;
		if(!kitPlayer.isInArena()) e.setCancelled(true);
		if(e.getRightClicked().getCustomName() != null
				&& e.getRightClicked().getCustomName().equals("§aJoin The Arena")) {
			Kits.getInstance().getArenaManager().warpIntoArena(p);
		}else if(e.getRightClicked().getCustomName() != null
				&& e.getRightClicked().getCustomName().equals("§aAvailable Kits")) {
			KitsGUI.openKitsGUI(p);
		}
	}
	
	@EventHandler
	public void onPreJoin(PlayerPreLoginEvent e) {
		KitPlayer kp = null;
		if(!Kits.getInstance().playerManager.getKitPlayers().containsKey(e.getUniqueId())) {
			kp = new KitPlayer(e.getUniqueId());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(!kp.isInArena()) p.teleport(Kits.getInstance().arenaManager.getArena().getLobbySpawn());
		if(ConfigUtils.getConfigSection("Messages").getBoolean("Tab-Prefix")) 
			p.setPlayerListName(Kits.getInstance().getExpManager().getLevel(kp.getExp()).getPrefix() + p.getPlayerListName());
		if(ConfigUtils.getConfigSection("Messages").getBoolean("Handle-Join-Leave-Messages"))
			e.setJoinMessage(Kits.getInstance().expManager.getPlayerLevel(kp).getPrefix() 
							+ Kits.getInstance().expManager.getPlayerLevel(kp).getNameColor() + p.getName() + "§f has joined the server");
		if(Kits.getInstance().getArenaManager().getArena().getMinBounds().equals(Kits.getInstance().getArenaManager().getArena().getMaxBounds())) {
			p.sendMessage("§7§m----------------------------------------");
			p.sendMessage("§a§lReminder:");
			p.sendMessage("§fThe arena hasn't been set up yet, use §a/arena help §fto get started.");
			p.sendMessage("§7§m----------------------------------------");
		}
		if(!Kits.getInstance().getEventManager().getShowdownEvent().isActive())
			p.setMaxHealth(20);
		//p.setScoreboard(Kits.getInstance().sbHelper.scoreBoard);
		
		/*int teamlvl = Kits.getInstance().getExpManager().getLevels().indexOf(Kits.getInstance().getExpManager().getLevel(kp.getExp()))+1;
		p.setLevel(teamlvl);
		double xpPercentage = (kp.getExp()-Kits.getInstance().getExpManager().getLevel(kp.getExp()).getRequiredExp())
		/(Kits.getInstance().getExpManager().getExpForNextLevel(kp)
		-Kits.getInstance().getExpManager().getLevel(kp.getExp()).getRequiredExp());
		p.sendMessage(xpPercentage + "");
		p.sendMessage(p.getExpToLevel() + "");
		p.setExp((float) (xpPercentage * p.getExp()));*/
		//TODO WHY DOES THIS NOT WORK - FIX EXP BAR LEVEL DISPLAY
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(ConfigUtils.getConfigSection("Messages").getBoolean("Handle-Join-Leave-Messages"))
			e.setQuitMessage(Kits.getInstance().expManager.getPlayerLevel(kp).getPrefix() 
							+ Kits.getInstance().expManager.getPlayerLevel(kp).getSecondaryColor() + p.getName() + "§f has left the server");
		if(kp.isInArena()) {
			kp.setInArena(false);
			kp.setKitSelected(false);
			kp.resetSelectedKit();
			kp.removeMobs();
			for(ItemStack is : p.getInventory().getContents()) {
				if(is != null)
					p.getWorld().dropItemNaturally(p.getLocation(), is);
			}
			for(ItemStack is : p.getInventory().getArmorContents()) {
				if(is != null && is.getType() != Material.AIR)
					p.getWorld().dropItemNaturally(p.getLocation(), is);
			}
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.setHealth(p.getMaxHealth());
			p.setFoodLevel(20);
			p.setSaturation(20);
			if(p.getKiller() == null)
				Bukkit.broadcastMessage(Kits.getInstance().expManager.getPlayerLevel(kp).getPrefix() 
						+ Kits.getInstance().expManager.getPlayerLevel(kp).getSecondaryColor() + p.getName() + "§f left and was killed");
			if(p.getKiller() != null) {
				Player killer = p.getKiller();
				KitPlayer killerKitPlayer = Kits.getInstance().getPlayerManager().getKitPlayer(killer.getUniqueId());
				if(killerKitPlayer.isInArena()) {
					if(killer.getHealth() < 16)
						killer.setHealth(p.getKiller().getHealth() + 4);
					else killer.setHealth(p.getKiller().getMaxHealth());
					killer.setFoodLevel(20);
					killer.setSaturation(20);
					ActionBarMessage.sendMessage(killer, "§c+2\u2764 §7(Kill)");
					Bukkit.broadcastMessage(Kits.getInstance().expManager.getPlayerLevel(kp).getPrefix() 
							+ Kits.getInstance().expManager.getPlayerLevel(kp).getSecondaryColor() + p.getName() + "§f left and was killed by "
							+ Kits.getInstance().expManager.getPlayerLevel(killerKitPlayer).getPrefix() 
						+ Kits.getInstance().expManager.getPlayerLevel(killerKitPlayer).getSecondaryColor() + killer.getName() + "§f");
				}
			}
		}
	}
	
	@EventHandler
	public void onDamageByPlayer(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(!Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId()).isInArena()) e.setCancelled(true);
		}else if(e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			if(proj.getShooter() instanceof Player) {
				Player p = (Player) proj.getShooter();
				if(!Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId()).isInArena()
						|| p == e.getEntity()) e.setCancelled(true);
			}
		}
		if(e.getEntity() instanceof Player)
			if(e.getFinalDamage() > ((Player)e.getEntity()).getHealth())
				if(e.getDamager() instanceof Wolf) {
					Player killer = (Player) ((Wolf)e.getDamager()).getOwner();
					if(killer.getHealth() < 16)
						killer.setHealth(killer.getHealth() + 4);
					else killer.setHealth(killer.getMaxHealth());
					killer.setFoodLevel(20);
					killer.setSaturation(20);
					//p.getKiller().sendMessage("§c+2\u2764 §7(Kill)");
					ActionBarMessage.sendMessage(killer, "§c+2\u2764 §7(Kill)");
				}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			if(!p.hasPermission("kits.admin") &&
					!Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId()).isInArena()) e.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		KitPlayer kp = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(kp.isInArena()) {
			kp.setInArena(false);
			kp.setKitSelected(false);
			kp.resetSelectedKit();
			kp.removeMobs();
			if(!this.itemDrops)
				e.getDrops().clear();
			spawnDeathChest(p, e);
		}
		
		Player killer = p.getKiller();
		if(killer != null && killer != p) {
			if(killer.getHealth() < 16)
				killer.setHealth(killer.getHealth() + 4);
			else killer.setHealth(killer.getMaxHealth());
			killer.setFoodLevel(20);
			killer.setSaturation(20);
			//p.getKiller().sendMessage("§c+2\u2764 §7(Kill)");
			ActionBarMessage.sendMessage(killer, "§c+2\u2764 §7(Kill)");
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		e.setRespawnLocation(Kits.getInstance().arenaManager.getArena().getLobbySpawn());
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		if(e.getFoodLevel() > p.getFoodLevel())
			return;
		if(e.getEntity().getHealth() < e.getEntity().getMaxHealth()/2)
			return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("kits.admin") &&
				!Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId()).isInArena())
			e.setCancelled(true);
		if(e.getItemDrop().getItemStack().getType().equals(Material.COMPASS))
			if(Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId()).isInArena()) {
				p.sendMessage("§cYou can't drop this item.");
				e.setCancelled(true);
			}
	}
	
	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock() == null) return;
		if(e.getClickedBlock().getType().equals(Material.WORKBENCH)
				&& allowWorkbench == false
				&& !e.getPlayer().hasPermission("kits.admin")) {
			e.setCancelled(true);
		}else if(e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)
				&& allowEnchtab == false
				&& !e.getPlayer().hasPermission("kits.admin")) {
			e.setCancelled(true);
		}else if(e.getClickedBlock().getType().equals(Material.ANVIL)
				&& allowAnvil == false
				&& !e.getPlayer().hasPermission("kits.admin")) {
			e.setCancelled(true);
		}
	}
    
    @EventHandler
    public void onDrinkPotion(PlayerItemConsumeEvent e) {
    	if(e.getItem() != null) {
    		ItemStack item = e.getItem();
    		if(item.getType() == Material.POTION) {
        		Player p = e.getPlayer();
        		new BukkitRunnable() {
        			public void run() {
                		p.getInventory().remove(Material.GLASS_BOTTLE);
        			}
        		}.runTaskLater(Kits.getInstance(), 1);
    		}
    	}
    }
    
    @EventHandler
    public void onSpawnSmallMob(PlayerInteractEntityEvent e) {
		if(e.getPlayer().getItemInHand() == null)
			return;
		if(e.getPlayer().getItemInHand().getType() == Material.MONSTER_EGG)
			e.setCancelled(true);
			return;
    }
    
    @EventHandler
    public void onTrackerUse(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand() == null)
			return;
		if(e.getPlayer().getItemInHand().getType() == Material.COMPASS)
			Kits.getInstance().getArenaManager().setCompassTarget(e.getPlayer());
			return;
    }
	
	@EventHandler
	public void onSpawnMobByPlayer(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if(e.getItem() == null)
			return;
		if(e.getItem().getType() != Material.MONSTER_EGG)
			return;
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(!kp.isInArena()) {
			e.setCancelled(true);
			return;
		}
		if(EntityType.fromId(e.getItem().getDurability()) == EntityType.HORSE){
			e.setCancelled(true);
			Horse horse = (Horse) e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(e.getBlockFace().getModX() + 0.5
					, e.getBlockFace().getModY(), e.getBlockFace().getModZ() + 0.5), EntityType.fromId(e.getItem().getDurability()));
			horse.setHealth(horse.getMaxHealth());
			horse.setOwner(p);
			horse.setTamed(true);
			horse.setAdult();
			horse.setDomestication(0);
			horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
			kp.addMob(horse);
			if(p.getItemInHand().getAmount() > 1)
				p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
			else if(p.getItemInHand().getAmount() == 1)
				p.getInventory().remove(p.getItemInHand());
		}else if(EntityType.fromId(e.getItem().getDurability()) == EntityType.WOLF){
			e.setCancelled(true);
			Wolf wolf = (Wolf) e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(e.getBlockFace().getModX() + 0.5
					, e.getBlockFace().getModY(), e.getBlockFace().getModZ() + 0.5), EntityType.fromId(e.getItem().getDurability()));
			wolf.setMaxHealth(wolf.getMaxHealth() * 3);
			wolf.setHealth(wolf.getMaxHealth());
			wolf.setOwner(p);
			wolf.setTamed(true);
			wolf.setAdult();
			wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
			kp.addMob(wolf);
			if(p.getItemInHand().getAmount() > 1)
				p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
			else if(p.getItemInHand().getAmount() == 1)
				p.getInventory().remove(p.getItemInHand());
		}else {
			e.setCancelled(true);
			Entity entity = e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(e.getBlockFace().getModX() + 0.5
				, e.getBlockFace().getModY(), e.getBlockFace().getModZ() + 0.5), EntityType.fromId(e.getItem().getDurability()));
			kp.addMob(entity);
			if(p.getItemInHand().getAmount() > 1)
				p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
			else if(p.getItemInHand().getAmount() == 1)
				p.getInventory().remove(p.getItemInHand());
		}
	}
	
	@EventHandler
	public void onEntityFire(EntityCombustEvent e) {
		if(e.getEntity().getType() == EntityType.ARMOR_STAND)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onEggSpawnChicken(PlayerEggThrowEvent e) {
		e.setHatching(false);
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(e.getPlayer().getUniqueId());
		if(e.getItem().getItemStack().getType().equals(Material.COMPASS))
			if(kp.isInArena()) {
				e.setCancelled(true);
				e.getItem().remove();
			}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			KitPlayer kp = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
			if(!kp.isInArena())
				e.setCancelled(true);
		}
	}
	
	private void spawnDeathChest(Player p, PlayerDeathEvent e) {
		if(!this.itemDrops)
			return;
		if(!this.lootchestOnBurn)
			return;
		if(p.getLocation().getBlock().getType() == Material.FIRE
				|| p.getLocation().getBlock().getType() == Material.LAVA
				|| p.getLocation().getBlock().getType() == Material.STATIONARY_LAVA) {
			Location loc = p.getLocation().add(0, 1, 0);
			p.getWorld().getBlockAt(loc).setType(Material.CHEST);
			Chest chest = (Chest) p.getWorld().getBlockAt(loc).getState();
			for(ItemStack i : e.getDrops())
				if(i.getType() != Material.COMPASS)
					chest.getInventory().addItem(i);
			ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(chest.getLocation().add(0.5, 0.8 - 2, 0.5), EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setVisible(false);
			as.setCustomNameVisible(true);
			as.setCustomName("§a15s");
			new BukkitRunnable() {
				int time = 14;
				public void run() {
					if(time > 10)
						as.setCustomName("§a" + time + "s");
					else if(time < 10 && time > 5)
						as.setCustomName("§e" + time + "s");
					else
						as.setCustomName("§c" + time + "s");
					if(time == 0) {
						p.getWorld().getBlockAt(loc).setType(Material.AIR);
						as.remove();
						this.cancel();
					}
					time--;
				}
			}.runTaskTimer(Kits.getInstance(), 20, 20);
		}
	}

}
