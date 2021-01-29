package me.syes.kits.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.commands.subcommands.ArenaNpcCommand;
import me.syes.kits.commands.subcommands.SetBoundsCommand;
import me.syes.kits.commands.subcommands.SetSpawnCommand;
import me.syes.kits.commands.subcommands.SetWorldCommand;
import me.syes.kits.commands.subcommands.SubCommand;
import me.syes.kits.commands.subcommands.WarpCommand;

public class ArenaCommandHandler implements CommandExecutor {
	
	private HashMap<String, SubCommand> commands;
	
	public ArenaCommandHandler() {
		this.commands = new HashMap<String, SubCommand>();
		registerCommands();
	}
	
	public void registerCommands() {
		commands.put("warp", new WarpCommand());
		commands.put("setspawn", new SetSpawnCommand());
		commands.put("setworld", new SetWorldCommand());
		commands.put("setbounds", new SetBoundsCommand());
		commands.put("npc", new ArenaNpcCommand());
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cCommands can only be executed by a player.");
			return true;
		}
		p = (Player) sender;
		if(args.length == 0 || (args[0] != null && args[0].equalsIgnoreCase("help"))) {
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
		sender.sendMessage("§cUnknown command, use /arena help for a list of arena commands.");
		return true;
	}

	private void sendHelpMenu(Player p) {
		p.sendMessage("§a§lAvailable Commands §7(v" + Kits.getInstance().getDescription().getVersion() + ")");
		p.sendMessage("§7> §f/arena warp");
		if(p.hasPermission("kits.admin")) {
			p.sendMessage("§dStaff Commands:");
			p.sendMessage("§7> §f/arena setspawn");
			p.sendMessage("§7> §f/arena setworld");
			p.sendMessage("§7> §f/arena setbounds §a<min/max>");
			p.sendMessage("§7> §f/arena npc §a<add/remove> §e<skullowner>");
		}
		p.sendMessage("§7");
	}

}
