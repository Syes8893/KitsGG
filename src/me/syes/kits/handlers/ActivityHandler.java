package me.syes.kits.handlers;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ActionBarMessage;
import me.syes.kits.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ActivityHandler implements Listener {

	private ArrayList<ItemStack> dropItems;

	public ActivityHandler(){
		dropItems = new ArrayList<>();
		loadItems();
	}

	@EventHandler
	public void onFish(PlayerFishEvent event){
		event.setExpToDrop(0);
		if(event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) || event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
//			event.getCaught().remove();
			Item item = (Item) event.getCaught();
			item.setItemStack(new ItemStack(Material.STICK));
			Player p = event.getPlayer();
			KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
			kp.setBonusExp(kp.getBonusExp()+1);
			ActionBarMessage.sendMessage(p, "§d+1 Exp §7(Caught Fish)");
		}
	}

	@EventHandler
	public void onMineOre(BlockBreakEvent e) {
		Player p = e.getPlayer();
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if (kp.isInArena() && e.getBlock().getType().equals(Material.DIAMOND_ORE)) {
			Random random = new Random();
			if (random.nextInt(10) == 0) {
				e.getPlayer().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 100.0F);
				kp.setBonusExp(kp.getBonusExp() + 1);
				ActionBarMessage.sendMessage(p, "§d+1 Exp §7(Mined Diamond)");
			}
			e.setCancelled(true);
		}
	}

	private void loadItems(){
		List<String> lore = new ArrayList<>();
		HashMap<Enchantment, Integer> enchants = new HashMap<>();
		enchants.put(Enchantment.DAMAGE_ALL, 2);
		this.dropItems.add(ItemUtils.buildEnchantedItem(new ItemStack(Material.IRON_SWORD), null, lore, enchants));
	}

}
