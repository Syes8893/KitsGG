package me.syes.kits.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.commands.subcommands.FinishEventCommand;
import me.syes.kits.commands.subcommands.RerollEventCommand;
import me.syes.kits.commands.subcommands.StartEventCommand;
import me.syes.kits.commands.subcommands.SubCommand;

public class EventCommandHandler implements CommandExecutor {
	
	private HashMap<String, SubCommand> commands;
	
	public EventCommandHandler() {
		this.commands = new HashMap<String, SubCommand>();
		registerCommands();
	}
	
	public void registerCommands() {
		commands.put("start", new StartEventCommand());
		commands.put("finish", new FinishEventCommand());
		commands.put("reroll", new RerollEventCommand());
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
		sender.sendMessage("§cUnknown command, use /event help for a list of commands.");
		return true;
	}

	private void sendHelpMenu(Player p) {
		if(p.hasPermission("kits.admin")) {
			p.sendMessage("§a§lAvailable Commands §7(v" + Kits.getInstance().getDescription().getVersion() + ")");
			p.sendMessage("§7> §f/event start");
			p.sendMessage("§7> §f/event finish");
			p.sendMessage("§7> §f/event reroll");
			p.sendMessage("§7");
		}
	}

}
