package me.syes.kits.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
import me.syes.kits.event.eventtypes.BossEvent;
import me.syes.kits.event.eventtypes.GoldRushEvent;
import me.syes.kits.event.eventtypes.KothEvent;
import me.syes.kits.event.eventtypes.MarksmanEvent;
import me.syes.kits.event.eventtypes.RamboEvent;
import me.syes.kits.event.eventtypes.ShowdownEvent;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;
import me.syes.kits.utils.MessageUtils;

public class EventManager {
	
	public ArrayList <Event> events;
	
	public RamboEvent ramboEvent;
	public MarksmanEvent marksmanEvent;
	public KothEvent kothEvent;
	public BossEvent bossEvent;
	public ShowdownEvent showdownEvent;
	public GoldRushEvent goldRushEvent;
	
	private int time;
	private Event nextEvent;
	
	public EventManager() {
		this.events = new ArrayList<Event>();
		this.events.add(ramboEvent = new RamboEvent(this));
		this.events.add(marksmanEvent = new MarksmanEvent(this));
		this.events.add(kothEvent = new KothEvent(this));
		this.events.add(bossEvent = new BossEvent(this));
		this.events.add(showdownEvent = new ShowdownEvent(this));
		this.events.add(goldRushEvent = new GoldRushEvent(this));
		eventTimer(ConfigUtils.getConfigSection("Event").getInt("Delay-Minutes"));
	}
	
	public void eventTimer(int period) {
		nextEvent = getRandomEvent();
		time = period * 60;
		new BukkitRunnable() {
			public void run() {
				time--;
				if(time == 0) {
					Event e = nextEvent;
					if(Bukkit.getOnlinePlayers().size() >= e.getRequiredPlayers())
						e.startEvent();
					else if(Bukkit.getOnlinePlayers().size() < e.getRequiredPlayers())
						MessageUtils.broadcastMessage("§cThe " + e.getName() + " Event has been cancelled due to a lack of players. ("
					+ Bukkit.getOnlinePlayers().size() + "/" + e.getRequiredPlayers() + ")");
					time = period * 60;
					nextEvent = getRandomEvent();
				}
			}
		}.runTaskTimerAsynchronously(Kits.getInstance(), 20, 20);
	}
	
	public Event getRandomEvent() {
		int i = new Random().nextInt(this.events.size());
		return this.events.get(i);
	}
	
	public Event getActiveEvent() {
		for(Event e : events) {
			if(e.isActive()) return e;
		}
		return null;
	}

	public RamboEvent getRamboEvent() {
		return ramboEvent;
	}

	public MarksmanEvent getMarksmanEvent() {
		return marksmanEvent;
	}

	public KothEvent getKothEvent() {
		return kothEvent;
	}

	public BossEvent getBossEvent() {
		return bossEvent;
	}

	public ShowdownEvent getShowdownEvent() {
		return showdownEvent;
	}

	public GoldRushEvent getGoldRushEvent() {
		return goldRushEvent;
	}
    
    public HashMap<Integer, KitPlayer> getEventTop() {
    	HashMap<Integer, KitPlayer> map = new HashMap<Integer, KitPlayer>();
    	Comparator<KitPlayer> sorter = new Comparator<KitPlayer>() {
			@Override
			public int compare(KitPlayer a, KitPlayer b) {
				if(getActiveEvent().getParticipantScore(a) > getActiveEvent().getParticipantScore(b)) return -1;
				return 1;
			}
    	};
    	ArrayList<KitPlayer> kitPlayers = new ArrayList<KitPlayer>();
    	for(KitPlayer kp : getActiveEvent().participants.keySet()) {
    		kitPlayers.add(kp);
    	}
    	kitPlayers.sort(sorter);
    	for(KitPlayer kp : kitPlayers) {
    		map.put(kitPlayers.indexOf(kp)+1, kp);
    	}
		return map;
    }
    
    public void skipTimeUntilNextEvent() {
    	time = 1;
    }
    
    public String getTimeUntilNextEvent() {
    	if(time > 60)
    		return time/60 + 1 + "m";
    	else if(time <= 60)
    		return time + "s";
    	return "ERROR";
    }

	public Event getEvent(String name){
		for(Event e : this.events){
			if(e.getName().equalsIgnoreCase(name))
				return e;
		}
		return null;
	}
    
    public Event getNextEvent() {
    	return nextEvent;
    }

	public void setNextEvent(Event nextEvent) {
		this.nextEvent = nextEvent;
	}
    
    public void rerollNextEvent() {
		nextEvent = getRandomEvent();
    }

}
