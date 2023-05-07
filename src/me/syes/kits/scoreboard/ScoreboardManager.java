package me.syes.kits.scoreboard;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.event.EventManager;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class ScoreboardManager extends BukkitRunnable
{
    private ScoreboardHandler scoreboardHandler;
    private String lines;
    private String separator;
    private String primaryColor;
    private String secondaryColor;
    
    public ScoreboardManager() {
        this.scoreboardHandler = new ScoreboardHandler();
        this.lines = "&7&m---------------------";
        this.separator = "&f";
        this.primaryColor = ConfigUtils.getConfigSection("Scoreboard").getString("Primary-Color");
        this.secondaryColor = ConfigUtils.getConfigSection("Scoreboard").getString("Secondary-Color");
    }
    
    public void run() {
    	if(!ConfigUtils.getConfigSection("Scoreboard").getBoolean("Enabled"))
    		return;
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            ScoreboardHelper board = this.scoreboardHandler.getScoreboard(p);
            KitPlayer kp = Kits.getInstance().getPlayerManager().getKitPlayers().get(p.getUniqueId());
        	EventManager eventManager = Kits.getInstance().getEventManager();
            board.clear();
            if(Kits.getInstance().getEventManager().getActiveEvent() == null) {
                board.add(this.lines);
                board.add(primaryColor + "Next Event");
                board.add("&d&l" + eventManager.getNextEvent().getName() + " &7(" + eventManager.getTimeUntilNextEvent() + ")");
                board.add(this.separator);
                board.add(primaryColor +  "Kills: " + secondaryColor + kp.getKills() + " &7(+" + kp.getKillstreak() + "&7)");
                board.add(primaryColor +  "Deaths: " + secondaryColor + kp.getDeaths());
                board.add(this.separator);
                board.add(primaryColor +  "Level: " + secondaryColor + Kits.getInstance().getExpManager().getLevel(kp.getExp()).getPrefix());
                if(Kits.getInstance().getExpManager().getExpForNextLevel(kp) > 0) {
                    board.add(primaryColor + "Progress: " + secondaryColor + (kp.getExp()-Kits.getInstance().getExpManager().getLevel(kp.getExp()).getRequiredExp())
                    		+ "/" + (Kits.getInstance().getExpManager().getExpForNextLevel(kp)
                    		-Kits.getInstance().getExpManager().getLevel(kp.getExp()).getRequiredExp()) + " Exp");
                    board.add(Kits.getInstance().getExpManager().getProgressBar(kp, 12, secondaryColor));
                }else
                    board.add(primaryColor + "Progress: " + secondaryColor + Kits.getInstance().getExpManager().getProgressBar(kp, 12, secondaryColor));
                board.add(this.separator);
                if(kp.getSelectedKit() != null)
                	board.add(primaryColor + "Kit: " + secondaryColor + kp.getSelectedKit().getName());
                else
                	board.add(primaryColor + "Kit: " + secondaryColor + "None");
                board.add(this.separator);
            	board.add(ConfigUtils.getConfigSection("Scoreboard").getString("Server-IP"));
                board.add(this.lines);
            }else {
                board.add(this.lines);
                board.add("&fCurrent Event");
                if(eventManager.getActiveEvent().getTimeLeftInteger() > 0) {
                	board.add("&d&l" + eventManager.getActiveEvent().getName() + " &7(" + eventManager.getActiveEvent().getTimeLeft() + ")");
                    board.add(this.separator);
                    for(int i = 1; i < 6; i ++) {
                    	if(i > eventManager.getActiveEvent().getParticipants().size()) break;
                        KitPlayer kp2 = Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(i));
                    	board.add("&7#" + i + "&f " + kp2.getName()
                    			+ ": &d" + new DecimalFormat("#.#").format(eventManager.getActiveEvent().getParticipantScore(eventManager.getEventTop().get(i))));
                    }
                    board.add(this.separator);
                	board.add(ConfigUtils.getConfigSection("Scoreboard").getString("Server-IP"));
                    board.add(this.lines);
                }else {
                	board.add("&d&l" + eventManager.getActiveEvent().getName() + " &7(ENDED)");
                	board.add(this.separator);
                    for(int i = 1; i < 4; i ++) {
                    	if(i > eventManager.getEventTop().size()) break;
                        KitPlayer kp2 = Kits.getInstance().getPlayerManager().getKitPlayers().get(eventManager.getEventTop().get(i));
                    	board.add("&7#" + i + " " + Kits.getInstance().getExpManager().getLevel(kp2.getExp()).getPrefix()
                    			+ kp2.getName());
                    }
                	board.add(this.separator);
                	board.add(ConfigUtils.getConfigSection("Scoreboard").getString("Server-IP"));
                	board.add(this.lines);
                }
            }
            /*int teamlvl = Kits.getInstance().getExpManager().getLevels().size() - Kits.getInstance().getExpManager().getLevels().indexOf(Kits.getInstance().getExpManager().getLevel(kp.getExp()));
            if(p.getScoreboard().getTeam("" + teamlvl) == null) {
            	Team t = p.getScoreboard().registerNewTeam("" + teamlvl);
            	t.setPrefix("" + teamlvl);
            	t.addPlayer(p);
            }else {
            	Team t = p.getScoreboard().getTeam(teamlvl + "");
            	t.addPlayer(p);
            }*/
            board.update(p);
        }
    }
    
    public static String convert(final int seconds) {
        final int h = seconds / 3600;
        final int i = seconds - h * 3600;
        final int m = i / 60;
        final int s = i - m * 60;
        String timeF = "";
        if (h < 10) {
            timeF = String.valueOf(timeF) + "0";
        }
        timeF = String.valueOf(timeF) + h + ":";
        if (m < 10) {
            timeF = String.valueOf(timeF) + "0";
        }
        timeF = String.valueOf(timeF) + m + ":";
        if (s < 10) {
            timeF = String.valueOf(timeF) + "0";
        }
        timeF = String.valueOf(timeF) + s;
        return timeF;
    }
    
    private String format(final double data) {
        final int minutes = (int)(data / 60.0);
        final int seconds = (int)(data % 60.0);
        final String str = String.format("%02d:%02d", minutes, seconds);
        return str;
    }
    
    public ScoreboardHandler getScoreboardHandler() {
    	return this.scoreboardHandler;
    }
}
