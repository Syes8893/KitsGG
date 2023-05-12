package me.syes.kits;

import me.syes.kits.handlers.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.syes.kits.arena.ArenaManager;
import me.syes.kits.commands.ArenaCommandHandler;
import me.syes.kits.commands.CommandHandler;
import me.syes.kits.commands.EventCommandHandler;
import me.syes.kits.commands.LevelCommandHandler;
import me.syes.kits.commands.StatsCommandHandler;
import me.syes.kits.event.EventListener;
import me.syes.kits.event.EventManager;
import me.syes.kits.experience.ExpListener;
import me.syes.kits.experience.ExpManager;
import me.syes.kits.gui.GUIManager;
import me.syes.kits.kit.KitManager;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.kitplayer.PlayerManager;
import me.syes.kits.leaderboard.LeaderboardManager;
import me.syes.kits.scoreboard.ScoreboardHelper;
import me.syes.kits.scoreboard.ScoreboardManager;
import me.syes.kits.utils.ArenaUtils;
import me.syes.kits.utils.ConfigUtils;
import me.syes.kits.utils.ExpUtils;
import me.syes.kits.utils.KitUtils;
import me.syes.kits.utils.LeaderboardUtils;
import me.syes.kits.utils.PlayerUtils;

public class Kits extends JavaPlugin {

	public static Kits instance;
	
	public KitManager kitManager;
	public PlayerManager playerManager;
	public ArenaManager arenaManager;
	public GUIManager guiManager;
	public LeaderboardManager leaderboardManager;
	public ScoreboardManager scoreboardManager;
	public EventManager eventManager;
	public ExpManager expManager;
	
	public static FileConfiguration config;
	
	public ScoreboardHelper sbHelper;

	public void onEnable() {
		//Load config first as for all settings to be applied
		ConfigUtils.loadConfig();
		
		//Initialise Managers
		kitManager = new KitManager();
		playerManager = new PlayerManager();
		arenaManager = new ArenaManager();
		guiManager = new GUIManager();
		eventManager = new EventManager();
		expManager = new ExpManager();
		ExpUtils.loadExpLevels();
		scoreboardManager = new ScoreboardManager();
		leaderboardManager = new LeaderboardManager();
		
		//Register Commands & Listeners
		getCommand("kits").setExecutor(new CommandHandler());
		getCommand("arena").setExecutor(new ArenaCommandHandler());
		getCommand("stats").setExecutor(new StatsCommandHandler());
		getCommand("event").setExecutor(new EventCommandHandler());
		getCommand("level").setExecutor(new LevelCommandHandler());
		getServer().getPluginManager().registerEvents(new WeatherHandler(), this);
		getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
		getServer().getPluginManager().registerEvents(new BlockHandler(), this);
		getServer().getPluginManager().registerEvents(new StatsHandler(), this);
		getServer().getPluginManager().registerEvents(new MessageHandler(), this);
		getServer().getPluginManager().registerEvents(new VelocityHandler(), this);
		getServer().getPluginManager().registerEvents(new InventoryHandler(), this);
		getServer().getPluginManager().registerEvents(new EntityHandler(), this);
		getServer().getPluginManager().registerEvents(new ActivityHandler(), this);
		getServer().getPluginManager().registerEvents(scoreboardManager.getScoreboardHandler(), this);
		
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		getServer().getPluginManager().registerEvents(new ExpListener(), this);
		
		//Load data
		KitUtils.loadKits();
		ArenaUtils.loadArenaData();
		PlayerUtils.loadPlayerData();
		//Temporary fix for
//		for(Entity e : arenaManager.getArena().getWorld().getEntities()){
//
//			if(e.getCustomName() == null)
//				continue;
//			if(!(e instanceof Player) && !(e.getType().equals(EntityType.ARMOR_STAND) && (e.getCustomName().equalsIgnoreCase("Join the Arena")
//					|| e.getCustomName().equalsIgnoreCase("Available Kits"))))
//				e.remove();
//		}
		LeaderboardUtils.loadLeaderboardData();
		
		//Fix no KitPlayer bug
		for(Player p : getServer().getOnlinePlayers()) {
			if(!Kits.getInstance().playerManager.getKitPlayers().containsKey(p.getUniqueId()))
				new KitPlayer(p.getUniqueId());
			if(p.getMaxHealth() != 20)
				p.setMaxHealth(20);
		}
		
		//Enable Scoreboard
		//this.sbHelper = new ScoreboardHelper(getServer().getScoreboardManager().getNewScoreboard(), "none");
		scoreboardManager.runTaskTimerAsynchronously(this, 20, 20);
	}
	
	public void onDisable() {
		//Save data
		KitUtils.saveKits();
		ArenaUtils.saveArenaData();
		PlayerUtils.savePlayerData();
		LeaderboardUtils.saveLeaderboardData();
		
		//Remove leaderboards to prevent double leaderboard bug
		leaderboardManager.removeLeaderboards(true);
	}
	
	public Kits() {
		instance = this;
	}
	
	public static Kits getInstance() {
		return instance;
	}
	
	public KitManager getKitManager() {
		return kitManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public GUIManager getGuiManager() {
		return guiManager;
	}

	public LeaderboardManager getLeaderboardManager() {
		return leaderboardManager;
	}

	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public ExpManager getExpManager() {
		return expManager;
	}
	
}
