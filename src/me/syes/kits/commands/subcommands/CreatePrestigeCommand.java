package me.syes.kits.commands.subcommands;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.InventoryUtils;
import me.syes.kits.utils.KitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreatePrestigeCommand extends SubCommand{

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
			if(kit.getName().equalsIgnoreCase(args[1])){
				if(Kits.getInstance().getKitManager().getKit(args[1] + "prestige") == null)
					break;
				kit.setHasPrestige(true);
				Kit k = new Kit(args[1] + "_prestige", InventoryUtils.getNamedInventoryHashMap(p, args[1], ""), InventoryUtils.getNamedArmourArray(p, args[1], ""), p.getItemInHand().clone(), 0, false);
				p.sendMessage("§aSuccessfully created the prestige for kit: " + k.getName().replace("_prestige", ""));
				return;
			}
		}
		for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
			if(kit.getName().equalsIgnoreCase(args[1] + "_prestige")) {
				kit.setItems(InventoryUtils.getNamedInventoryHashMap(p, kit.getName(), ""));
				kit.setArmour(InventoryUtils.getNamedArmourArray(p, kit.getName(), ""));
				if(p.getItemInHand() == null)
					kit.setIcon(new ItemStack(Material.COBBLESTONE));
				else
					kit.setIcon(p.getItemInHand().clone());
				kit.setName(args[1] + "_prestige");
				KitUtils.saveKit(kit);
				p.sendMessage("§aSuccessfully updated the prestige for kit: " + args[1].toString());
				return;
			}
		}
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit createprestige <name>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
