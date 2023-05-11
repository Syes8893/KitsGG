package me.syes.kits.commands.subcommands.arenacommands;

import me.syes.kits.commands.subcommands.SubCommand;
import me.syes.kits.utils.ConfigUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.syes.kits.utils.ItemUtils;

public class ArenaNpcCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		if(args[1].equalsIgnoreCase("add")) {
			ArmorStand as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
			as.setCustomName(ConfigUtils.getConfigSection("NPC").getString("Name-Color").replace("&", "§") + "Join The Arena");
			as.setCustomNameVisible(true);
			as.setBasePlate(false);
			as.setArms(true);
			as.setItemInHand(p.getItemInHand().clone());
			as.setHelmet(ItemUtils.buildHead("Steve"));
			if(args.length > 2)
				as.setHelmet(ItemUtils.buildHead(args[2]));
			as.setChestplate(p.getInventory().getArmorContents()[2].clone());
			as.setLeggings(p.getInventory().getArmorContents()[1].clone());
			as.setBoots(p.getInventory().getArmorContents()[0].clone());
			p.sendMessage("§aSuccessfully created an NPC at your current location.");
		}else if(args[1].equalsIgnoreCase("remove")) {
			for(Entity e : p.getNearbyEntities(3, 3, 3)) {
				if(e.getType() == EntityType.ARMOR_STAND 
						&& (e.getCustomName() != null && e.getCustomName().equalsIgnoreCase(ConfigUtils.getConfigSection("NPC").getString("Name-Color").replace("&", "§") + "Join The Arena"))) e.remove();
			}
			p.sendMessage("§aSuccessfully removed all nearby NPCs.");
		}
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /arena npc <add/remove> <skullowner>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
