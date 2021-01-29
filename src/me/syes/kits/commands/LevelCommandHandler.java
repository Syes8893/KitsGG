package me.syes.kits.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.syes.kits.Kits;
import me.syes.kits.commands.subcommands.SubCommand;
import me.syes.kits.gui.LevelGUI;
import me.syes.kits.gui.StatsGUI;
import me.syes.kits.kitplayer.KitPlayer;

public class LevelCommandHandler implements CommandExecutor {
	
	private HashMap<String, SubCommand> commands;
	
	public LevelCommandHandler() {
		this.commands = new HashMap<String, SubCommand>();
		registerCommands();
	}
	
	public void registerCommands() {
		//Leave for future reference
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
			LevelGUI.openLevelGUI(p, kp);
			return true;
		}else if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
			sendHelpMenu(p);
			return true;
		}else if(args.length == 1 && Bukkit.getOfflinePlayer(args[0]) != null) {
			if(Kits.getInstance().getPlayerManager().getKitPlayers().get(Bukkit.getOfflinePlayer(args[0]).getUniqueId()) != null)
				LevelGUI.openLevelGUI(p, Kits.getInstance().getPlayerManager().getKitPlayers().get(Bukkit.getOfflinePlayer(args[0]).getUniqueId()));
			else p.sendMessage("§cNo stats found for " + Bukkit.getOfflinePlayer(args[0]).getName() + ".");
			return true;
		}else {
			p.sendMessage("§cUsage: /level <player>");
		}
		return false;
	}

	private void sendHelpMenu(Player p) {
		p.sendMessage("§a§lAvailable Commands §7(v" + Kits.getInstance().getDescription().getVersion() + ")");
		p.sendMessage("§7> §f/level §e<player>");
		p.sendMessage("§7");
	}

}
