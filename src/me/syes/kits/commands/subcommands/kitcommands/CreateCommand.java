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
		String prefix = "";
		if(args.length > 2)
			level = Integer.parseInt(args[2]);
		if(args.length > 3)
			prefix = args[3] + " ";
		for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
			if(kit.getName().equalsIgnoreCase(args[1]) && kit.getLevel() == level) {
				kit.setItems(InventoryUtils.getNamedInventoryHashMap(p, kit.getName(), prefix));
				kit.setArmour(InventoryUtils.getNamedArmourArray(p, kit.getName(), prefix));
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
		Kit k = new Kit(args[1], InventoryUtils.getNamedInventoryHashMap(p, args[1], prefix), InventoryUtils.getNamedArmourArray(p, args[1], prefix)
				, p.getItemInHand().clone(), 0, false, level);
		p.sendMessage("§aSuccessfully created the kit: " + k.getNameAndLevel() + ".");
		if(level > 1)
			Kits.getInstance().getKitManager().getKit(args[1], k.getLevel()-1).setHasUpgrade(true);
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit create <name> [level] [prefix]");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
