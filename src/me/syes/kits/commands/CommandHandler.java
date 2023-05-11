package me.syes.kits.commands;

import java.util.HashMap;

import me.syes.kits.commands.subcommands.*;
import me.syes.kits.commands.subcommands.kitcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.gui.KitsGUI;
import me.syes.kits.kit.Kit;
import me.syes.kits.kitplayer.KitPlayer;

public class CommandHandler implements CommandExecutor {
	
	private HashMap<String, SubCommand> commands;
	
	public CommandHandler() {
		this.commands = new HashMap<String, SubCommand>();
		registerCommands();
	}
	
	public void registerCommands() {
		commands.put("create", new CreateCommand());
		commands.put("delete", new DeleteCommand());
		commands.put("seticon", new SetIconCommand());
		SetRequiredExpCommand setRequiredExpCommand = new SetRequiredExpCommand();
		commands.put("setexp", setRequiredExpCommand);
		commands.put("setrequiredexp", setRequiredExpCommand);
		commands.put("list", new ListCommand());
		commands.put("update", new UpdateCommand());
		commands.put("npc", new KitNpcCommand());
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cCommands can only be executed by a player.");
			return true;
		}
		p = (Player) sender;
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayer(p.getUniqueId());
		if(args.length == 0) {
			if(kp.isInArena()) {
				p.sendMessage("§cYou can't select a kit whilst in the arena.");
				return true;
			}
			KitsGUI.openKitsGUI(p);
			return true;
		}
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")) {
			sendHelpMenu(p);
			return true;
		}
		for(String str : commands.keySet())
			if(str.equalsIgnoreCase(args[0])) {
				if(!p.hasPermission(commands.get(str).permission())) {
					p.sendMessage("§cInsufficient permission to execute this command.");
					return true;
				}
				commands.get(str).execute(p, args);
				return true;
			}
		if(kp.isInArena()) {
			p.sendMessage("§cYou can't select a kit whilst in the arena.");
			return true;
		}
		if(args[0].equalsIgnoreCase("random")) {
			Kits.getInstance().getKitManager().giveRandomKit(p);
			return true;
		}
		for(Kit kit : Kits.getInstance().getKitManager().getKits()) {
			if(args[0].equalsIgnoreCase(kit.getName())) {
				Kits.getInstance().getKitManager().giveKit(p, kit);
				return true;
			}
		}
		sender.sendMessage("§cUnknown kit, use /kit list for a list of all kits.");
		return true;
	}

	private void sendHelpMenu(Player p) {
		p.sendMessage("§a§lAvailable Commands §7(v" + Kits.getInstance().getDescription().getVersion() + ")");
		p.sendMessage("§7> §f/kit §e<kitname>");
		p.sendMessage("§7> §f/kit list");
		if(p.hasPermission("kits.admin")) {
			p.sendMessage("§dStaff Commands:");
			p.sendMessage("§7> §f/kit create §a<kitname> §e[level]");
			p.sendMessage("§7> §f/kit delete §a<kitname> §e[level]");
			p.sendMessage("§7> §f/kit seticon §a<kitname> §e[level]");
			p.sendMessage("§7> §f/kit setrequiredexp §a<kitname> <amount> §e[level]");
			p.sendMessage("§7> §f/kit npc §a<add/remove> §e<skullowner>");
			p.sendMessage("§7> §f/kit update §7(Use after updating)");
		}
		p.sendMessage("§7");
	}

}
