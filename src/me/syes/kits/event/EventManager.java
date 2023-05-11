package me.syes.kits.event;

import java.util.*;

import me.syes.kits.event.eventtypes.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.syes.kits.Kits;
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
	public PaintballEvent paintballEvent;
	public SkyFightEvent skyFightEvent;
	public EnderHuntEvent enderHuntEvent;
	
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
		this.events.add(paintballEvent = new PaintballEvent(this));
		this.events.add(skyFightEvent = new SkyFightEvent(this));
		this.events.add(enderHuntEvent = new EnderHuntEvent(this));
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
		}.runTaskTimer(Kits.getInstance(), 20, 20);
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

	public PaintballEvent getPaintballEvent() {
		return paintballEvent;
	}
	public SkyFightEvent getSkyFightEvent() {
		return skyFightEvent;
	}
	public EnderHuntEvent getEnderHuntEvent() {
		return enderHuntEvent;
	}
    
    public HashMap<Integer, UUID> getEventTop() {
    	HashMap<Integer, UUID> map = new HashMap<Integer, UUID>();
    	Comparator<UUID> sorter = new Comparator<UUID>() {
			@Override
			public int compare(UUID a, UUID b) {
				if(getActiveEvent().getParticipantScore(a) > getActiveEvent().getParticipantScore(b)) return -1;
				return 1;
			}
    	};
    	ArrayList<UUID> kitPlayers = new ArrayList<UUID>();
    	for(UUID uuid : getActiveEvent().participants.keySet()) {
    		kitPlayers.add(uuid);
    	}
    	kitPlayers.sort(sorter);
    	for(UUID uuid : kitPlayers) {
    		map.put(kitPlayers.indexOf(uuid)+1, uuid);
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
