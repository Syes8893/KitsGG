package me.syes.kits.commands.subcommands.kitcommands;

import me.syes.kits.commands.subcommands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.InventoryUtils;
import me.syes.kits.utils.KitUtils;

public class CreateCommand extends SubCommand {

	@Override
	public void execute(Player p, String[] args) {
		if(args.length < 2) {
			help(p);
			return;
		}
		int level = 1;
		if(args[2] != null)
			level = Integer.parseInt(args[2]);
		for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
			if(kit.getName().equalsIgnoreCase(args[1]) && kit.getLevel() == level) {
				kit.setItems(InventoryUtils.getNamedInventoryHashMap(p, kit.getName(), ""));
				kit.setArmour(InventoryUtils.getNamedArmourArray(p, kit.getName(), ""));
				if(p.getItemInHand() == null)
					kit.setIcon(new ItemStack(Material.COBBLESTONE));
				else
					kit.setIcon(p.getItemInHand().clone());
				kit.setName(args[1]);
				KitUtils.saveKit(kit);
				p.sendMessage("§aSuccessfully updated the kit: " + kit.getNameAndLevel() + ".");
				return;
			}
		}
		Kit k = new Kit(args[1], InventoryUtils.getNamedInventoryHashMap(p, args[1], ""), InventoryUtils.getNamedArmourArray(p, args[1], "")
				, p.getItemInHand().clone(), 0, false, level);
		p.sendMessage("§aSuccessfully created the kit: " + k.getNameAndLevel() + ".");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit create <name> [level]");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
