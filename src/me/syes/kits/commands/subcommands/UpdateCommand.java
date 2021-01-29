package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.syes.kits.Kits;
import me.syes.kits.kit.Kit;
import me.syes.kits.utils.ItemUtils;

public class UpdateCommand extends SubCommand{

	@Override
	public void execute(Player p, String[] args) {
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			for(Object i : k.getItems().keySet().toArray())
				if((Integer)i >= p.getInventory().getSize())
					k.getItems().remove(i);
			for(ItemStack is : k.getItems().values()) is = ItemUtils.nameItem(is, k.getName(), "");
			for(ItemStack is : k.getArmour()) is = ItemUtils.nameItem(is, k.getName(), "");
		}
		p.sendMessage("§aSuccessfully updated all kits.");
	}

	@Override
	public void help(Player p) {
		p.sendMessage("§cUsage: /kit update");
	}

	@Override
	public String permission() {
		return "kits.admin";
	}

}
