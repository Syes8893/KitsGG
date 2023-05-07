package me.syes.kits.event.eventtypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.arena.Arena;
import me.syes.kits.event.Event;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;
import me.syes.kits.utils.ItemUtils;
import me.syes.kits.utils.MessageUtils;

public class BossEvent extends Event {
	
	private LivingEntity boss;
	private int health;
	private int trackingRange;
	
	private ArrayList<Entity> minions;
	
	public BossEvent(EventManager eventManager) {
		this.eventManager = eventManager;
		this.participants = new HashMap<KitPlayer, Double>();
		this.name = "Boss";
		this.goal = "Deal the most damage to the boss to win.";
		this.rules = "No rules apply for this event.";
		
		this.health = ConfigUtils.getConfigSection("Event.Boss").getInt("Health");
		this.trackingRange = ConfigUtils.getConfigSection("Event.Boss").getInt("Tracking-Range");
		
		minions = new ArrayList<Entity>();	
	}
	
	@Override
	public void startEvent() {
		this.announceEventStart();
		this.loadParticipants();
		this.setActive(true);
		this.time = this.durationSeconds;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				setupRandomBoss();
			}
		}.runTask(Kits.getInstance());
		
		new BukkitRunnable() {
			public void run() {
				//setNearbyTarget();
				if(!isActive()) {
					this.cancel();
					return;
				}
				if(time < durationSeconds && time%10 == 0 && time > 0) {
					bossAbility();
				}
				if(time%30 == 0 && time > 0) {
					MessageUtils.broadcastMessage("&fThe boss is located at: &dX:" + boss.getLocation().getBlockX()
							+ " Y:" + boss.getLocation().getBlockY() + " Z:" + boss.getLocation().getBlockZ() + " &7(Use the compass to track it down!)");
				}
				if(time == 0 || boss.isDead()) {
					finishEvent();
					this.cancel();
					time++;
				}
				time--;
			}
		}.runTaskTimer(Kits.getInstance(), 0, 20);
	}

	@Override
	public void finishEvent() {
		this.announceEventEnd();
		setActive(false);
		this.participants.clear();
		this.boss.remove();
		for(Entity e : minions)
			e.remove();
		time = durationSeconds;
		resetArena();
	}
	
	public void setupRandomBoss() {
		int r = new Random().nextInt(3);
		Arena a = Kits.getInstance().getArenaManager().getArena();
		if(r == 0) {
			MagmaCube alfie = (MagmaCube) a.getWorld().spawnEntity(a.getRandomSpawn(), EntityType.MAGMA_CUBE);
			alfie.setSize(5);
			alfie.setMaxHealth(this.health);
			alfie.setHealth(alfie.getMaxHealth());
			alfie.setCustomName("§cAlfie the Infernal");
			alfie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 10, true));
			alfie.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 4, true));
			alfie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 9, true));
			boss = alfie;
		}else if(r == 1) {
			Zombie karl = (Zombie) a.getWorld().spawnEntity(a.getRandomSpawn(), EntityType.ZOMBIE);
			karl.setBaby(false);
			karl.setMaxHealth(this.health);
			karl.setHealth(karl.getMaxHealth());
			karl.setCustomName("§2Karl The Fallen");
			karl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, true));
			karl.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, true));
			karl.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true));
			//Set Items
			HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
			enchants.put(Enchantment.THORNS, 3);
			enchants.put(Enchantment.DAMAGE_ALL, 5);
			enchants.put(Enchantment.KNOCKBACK, 2);
			enchants.put(Enchantment.DURABILITY, 10000);
			karl.getEquipment().setItemInHand(ItemUtils.buildEnchantedItem(new ItemStack(Material.WOOD_SWORD)
					, "§2Karl's Wooden-Carved Katana", Arrays.asList(""), enchants));
			enchants.clear();
			enchants.put(Enchantment.DEPTH_STRIDER, 10);
			enchants.put(Enchantment.DURABILITY, 100);
			karl.getEquipment().setBoots(ItemUtils.buildEnchantedItem(new ItemStack(Material.DIAMOND_BOOTS), "§2Karl's Nikes", Arrays.asList(""), enchants));
			boss = karl;
		}else if(r == 2) {
			Skeleton skeleton = (Skeleton) a.getWorld().spawnEntity(a.getCenter(), EntityType.SKELETON);
			//skeleton.setPassenger(a.getWorld().spawnEntity(skeleton.getLocation(), EntityType.SKELETON));
			skeleton.setSkeletonType(SkeletonType.NORMAL);
			skeleton.setMaxHealth(this.health);
			skeleton.setHealth(skeleton.getMaxHealth());
			skeleton.setCustomName("§eVictor the Undead");
			skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, true));
			skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, true));
			skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true));
			//Set Items
			HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
			enchants.put(Enchantment.ARROW_DAMAGE, 8);
			enchants.put(Enchantment.ARROW_INFINITE, 5);
			skeleton.getEquipment().setItemInHand(ItemUtils.buildEnchantedItem(new ItemStack(Material.BOW)
					, "§eVictor's Ancient Treasure", Arrays.asList(""), enchants));
			boss = skeleton;
			Horse horse = (Horse) a.getWorld().spawnEntity(a.getCenter(), EntityType.HORSE);
			horse.setVariant(Horse.Variant.SKELETON_HORSE);
			horse.setPassenger(skeleton);
		}
	}
	
	public void bossAbility() {
		int r = new Random().nextInt(5);
		for(Entity e : boss.getNearbyEntities(10, 5, 10))
			if(e instanceof Player) {
				Player p = (Player) e;
				KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
				if(kp.isInArena()) {
					if(r == 0) {
						//Lightning Ability
						p.damage(3, boss);
						p.getWorld().strikeLightningEffect(p.getLocation().add(0, -1, 0));
						p.setFireTicks(80);
						MessageUtils.broadcastMessage(boss.getCustomName() + " §fused ability: §dLightning Strike");
					}else if(r == 1) {
						//Root Ability
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 9));
			            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 180));
			            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
						MessageUtils.broadcastMessage(boss.getCustomName() + " §fused ability: §dRoot");
					}else if(r == 2) {
						//Intoxicate Ability
						p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 2));
						MessageUtils.broadcastMessage(boss.getCustomName() + " §fused ability: §dIntoxicate");
					}else if(r == 3) {
						//Summon Minions Ability
						LivingEntity minion;
						minions.add(minion = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), boss.getType()));
						minion.setCustomName(boss.getCustomName() + "'s Minion");
						minion.setMaxHealth(40);
						minion.setHealth(minion.getMaxHealth());
						for(PotionEffect pe : boss.getActivePotionEffects())
							minion.addPotionEffect(pe);
						MessageUtils.broadcastMessage(boss.getCustomName() + " §fused ability: §dSummon Minions");
					}else if(r == 4) {
						//Swap Ability
						Location playerLoc = p.getLocation();
						Location bossLoc = boss.getLocation();
						p.teleport(bossLoc);
						boss.teleport(playerLoc);
						p.sendMessage("§7§oYou've swapped locations with the boss!");
						MessageUtils.broadcastMessage(boss.getCustomName() + " §fused ability: §dLocation Swap");
						break;
					}
				}
			}
		
		/*boss.getWorld().strikeLightningEffect(boss.getLocation().add(0, -0.75, 0));
		for(Entity e : boss.getNearbyEntities(8, 5, 8))
			if(e instanceof Player)
				((Player) e).damage(3);
		
		for(Entity e : boss.getNearbyEntities(8, 5, 8))
			if(e instanceof Player)
				e.getWorld().spawnEntity(e.getLocation(), EntityType.ZOMBIE);*/
	}
	
	/*public void setNearbyTarget() {
		if(((Creature) boss).getTarget() == null) {
			for(Entity e : boss.getNearbyEntities(this.trackingRange, this.trackingRange, this.trackingRange)) {
				if(e instanceof Player)
					if(Kits.getInstance().getPlayerManager().getKitPlayer(e.getUniqueId()).isInArena()) {
						((Creature) boss).setTarget((LivingEntity) e);
						return;
					}
			}
		}
	}*/
	
	public Entity getBoss() {
		return boss;
	}

}
