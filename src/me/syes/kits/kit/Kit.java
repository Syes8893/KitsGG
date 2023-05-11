package me.syes.kits.kit;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.InventoryUtils;
import me.syes.kits.utils.ItemUtils;

public class Kit {
	
	private String name;
	private HashMap<Integer, ItemStack> items;
	private ItemStack[] armour;
	private ItemStack icon;
	
	private int requiredExp;

	private boolean hasPrestige;
	
	public Kit(String name, HashMap<Integer, ItemStack> items, ItemStack[] armour, ItemStack icon, int requiredExp, boolean hasPrestige) {
		this.name = name;
//		if(this.name.contains("_prestige"))
//			this.name = this.name.replace("_prestige", "");
		this.items = items;
		this.armour = armour;
		this.icon = icon;
		this.requiredExp = requiredExp;
		this.hasPrestige = hasPrestige;
		Kits.getInstance().getKitManager().addKit(this);
	}
	
	public void giveKit(Player p) {
		KitPlayer kp = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		p.getInventory().clear();
		p.setHealth(p.getMaxHealth());
		for(PotionEffect pe : p.getActivePotionEffects()) p.removePotionEffect(pe.getType());
		p.getInventory().setArmorContents(armour.clone());
		//ItemUtils.renameItems(this.items.values(), this, "&6[Prestige] ");
		for(int slot : items.keySet()) {
			p.getInventory().setItem(slot, items.get(slot));
		}
		kp.setKitSelected(true);
		kp.setSelectedKit(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Integer, ItemStack> getItems() {
		return items;
	}

	public void setItems(HashMap<Integer, ItemStack> items) {
		this.items = items;
	}

	public ItemStack[] getArmour() {
		return armour;
	}

	public void setArmour(ItemStack[] armour) {
		this.armour = armour;
	}

	public ItemStack getIcon() {
		if(icon == null || icon.getType().equals(Material.AIR))
			return new ItemStack(Material.COBBLESTONE);
		return icon.clone();
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public int getRequiredExp() {
		return requiredExp;
	}

	public void setRequiredExp(int requiredExp) {
		this.requiredExp = requiredExp;
	}

	public boolean hasPrestige() {
		return hasPrestige;
	}

	public void setHasPrestige(boolean hasPrestige) {
		this.hasPrestige = hasPrestige;
	}
}
