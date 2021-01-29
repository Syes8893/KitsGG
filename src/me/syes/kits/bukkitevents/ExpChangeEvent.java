package me.syes.kits.bukkitevents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.syes.kits.kitplayer.KitPlayer;

public class ExpChangeEvent extends Event {
	
    private static HandlerList handlers;
    private KitPlayer kitPlayer;
    private Player player;
    private int expChanged;
    
    static {
    	ExpChangeEvent.handlers = new HandlerList();
    }
    
    public HandlerList getHandlers() {
        return ExpChangeEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return ExpChangeEvent.handlers;
    }
    
    public ExpChangeEvent(KitPlayer kitPlayer, int expChanged) {
        this.kitPlayer = kitPlayer;
        this.player = Bukkit.getPlayer(kitPlayer.getUuid());
        this.expChanged = expChanged;
    }

	public KitPlayer getKitPlayer() {
		return kitPlayer;
	}

	public void setKitPlayer(KitPlayer kitPlayer) {
		this.kitPlayer = kitPlayer;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getExpChanged() {
		return expChanged;
	}

	public void setExpChanged(int expChanged) {
		this.expChanged = expChanged;
	}
    
}
