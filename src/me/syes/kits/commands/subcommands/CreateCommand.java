package me.syes.kits.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.InventoryUtils;
import me.syes.kits.utils.KitUtils;

public class CreateCommand extends SubCommand{

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
			if(kit.getName().equalsIgnoreCase(args[1])) {
				kit.setItems(InventoryUtils.getNamedInventoryHashMap(p, kit.getName(), ""));
				kit.setArmour(InventoryUtils.getNamedArmourArray(p, kit.getName(), ""));
				if(p.getItemInHand() == null)
					kit.setIcon(new ItemStack(Material.COBBLESTONE));
				else
					kit.setIcon(p.getItemInHand().clone());
				kit.setName(args[1]);
				KitUtils.saveKit(kit);
				p.sendMessage("§aSuccesfully updated the kit: " + args[1].toString());
				return;
			}
		}
		Kit k = new Kit(args[1], InventoryUtils.getNamedInventoryHashMap(p, args[1], ""), InventoryUtils.getNamedArmourArray(p, args[1], "")
				, p.getItemInHand().clone(), 0, false);
		p.sendMessage("§aSuccesfully created the kit: " + k.getName());
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit create <name>");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
