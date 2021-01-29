package me.syes.kits.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.commands.subcommands.LeaderboardCommand;
import me.syes.kits.commands.subcommands.ResetStatsCommand;
import me.syes.kits.commands.subcommands.SetStatsCommand;
import me.syes.kits.commands.subcommands.SubCommand;
import me.syes.kits.gui.StatsGUI;
import me.syes.kits.kitplayer.KitPlayer;

public class StatsCommandHandler implements CommandExecutor {
	
	private HashMap<String, SubCommand> commands;
	
	public StatsCommandHandler() {
		this.commands = new HashMap<String, SubCommand>();
		registerCommands();
	}
	
	public void registerCommands() {
		commands.put("reset", new ResetStatsCommand());
		commands.put("leaderboard", new LeaderboardCommand());
		commands.put("lb", new LeaderboardCommand());
		commands.put("set", new SetStatsCommand());;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cCommands can only be executed by a player.");
			return true;
		}
		p = (Player) sender;
		KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
		if(args.length == 0) {
			StatsGUI.openStatsGUI(p, kp);
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
		if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
			sendHelpMenu(p);
			return true;
		}else if(args.length == 1 && Bukkit.getOfflinePlayer(args[0]) != null) {
			if(Kits.getInstance().getPlayerManager().getKitPlayers().get(Bukkit.getOfflinePlayer(args[0]).getUniqueId()) != null)
				StatsGUI.openStatsGUI(p, Kits.getInstance().getPlayerManager().getKitPlayers().get(Bukkit.getOfflinePlayer(args[0]).getUniqueId()));
			else p.sendMessage("§cNo stats found for " + Bukkit.getOfflinePlayer(args[0]).getName() + ".");
			return true;
		}
		sender.sendMessage("§cUnknown command, use /stats help for a list of statistic commands.");
		return true;
	}

	private void sendHelpMenu(Player p) {
		p.sendMessage("§a§lAvailable Commands §7(v" + Kits.getInstance().getDescription().getVersion() + ")");
		p.sendMessage("§7> §f/stats §e<player>");
		p.sendMessage("§7> §f/stats level");
		if(p.hasPermission("kits.admin")) {
			p.sendMessage("§dStaff Commands:");
			p.sendMessage("§7> §f/stats reset §a<player>");
			p.sendMessage("§7> §f/stats lb §a<add/remove> §e<type/radius>");
			p.sendMessage("§7> §f/stats set §a<player> <kills/deaths/eventplayed/eventswon> <amount>");
		}
		p.sendMessage("§7");
	}

}
