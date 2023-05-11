package me.syes.kits.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.syes.kits.utils.ActionBarMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class BlockHandler implements Listener {

	public List<Block> placedBlocks;
	public List<Material> placeableBlocks;
//	public List<Material> breakableBlocks;

	private int blockDisappearSeconds;

	public BlockHandler() {
		placedBlocks = new ArrayList<Block>();
		placeableBlocks = new ArrayList<Material>();
//		breakableBlocks = new ArrayList<Material>();

		for (String str : ConfigUtils.getConfigSection("Arena.Building").getStringList("Placeable-Blocks")) {
			if (Material.getMaterial(str.replace(" ", "_").toUpperCase()) == null)
				System.out.println("[ERROR] Unknown material for entry: " + str + " (At Arena: Building: Placeable-Blocks: " + str + ")");
			placeableBlocks.add(Material.getMaterial(str.replace(" ", "_").toUpperCase()));
		}
//		for(String str : ConfigUtils.getConfigSection("Arena.Building").getStringList("Breakable-Blocks"))
//			breakableBlocks.add(Material.getMaterial(str));

		blockDisappearSeconds = ConfigUtils.getConfigSection("Arena.Building").getInt("Block-Disappear-Seconds");
	}

	@EventHandler
	public void onFireBreak(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if (!placedBlocks.contains(p.getTargetBlock((Set<Material>) null, 5)) && kp.isInArena())
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK))
				if (!e.getClickedBlock().getType().equals(Material.DIAMOND_ORE)) {
					e.setCancelled(true);
				}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if (kp.isInArena() && placedBlocks.contains(e.getBlock())) {
			placedBlocks.remove(e.getBlock());
			return;
		}
		if (kp.isInArena() && e.getBlock().getType().equals(Material.DIAMOND_ORE)) {
			Random random = new Random();
			if (random.nextInt(10) == 0) {
				e.getPlayer().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 100.0F);
				kp.setBonusExp(kp.getBonusExp() + 1);
				ActionBarMessage.sendMessage(p, "§d+1 Exp §7(Mined Diamonds)");
			}
			e.setCancelled(true);
		}
		if (!e.getPlayer().hasPermission("kits.build")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(kp.isInArena())
			if(this.placeableBlocks.contains(e.getBlockPlaced().getType())) {
				if(e.getBlockReplacedState().getType() == Material.STATIONARY_WATER
						|| e.getBlockReplacedState().getType() == Material.WATER
						|| e.getBlockReplacedState().getType() == Material.STATIONARY_LAVA
						|| e.getBlockReplacedState().getType() == Material.LAVA) {
					e.setCancelled(true);
					return;
				}
				if(placedBlocks.contains(p.getTargetBlock((Set<Material>) null, 5)))
					return;
				if(placedBlocks.contains(e.getBlockReplacedState().getBlock()))
					return;
				this.placedBlocks.add(e.getBlockPlaced());
				new BukkitRunnable() {
					public void run() {
						if(placedBlocks.contains(e.getBlockPlaced())) {
							e.getBlockPlaced().setType(e.getBlockReplacedState().getType());
							e.getBlockPlaced().setData(e.getBlockReplacedState().getRawData());
							placedBlocks.remove(e.getBlockPlaced());
						}
					}
				}.runTaskLater(Kits.getInstance(), this.blockDisappearSeconds * 20);
				return;
			}
		if(!e.getPlayer().hasPermission("kits.build")) e.setCancelled(true);
	}

}
