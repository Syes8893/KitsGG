package me.syes.kits.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import me.syes.kits.utils.ConfigUtils;

public class VelocityHandler implements Listener{
	
	@EventHandler
	public void onVelocity(PlayerVelocityEvent e) {
		if(!ConfigUtils.getConfigSection("Velocity").getBoolean("Modify-Velocity"))
			return;
		e.setVelocity(new Vector(e.getPlayer().getVelocity().getX() * ConfigUtils.getConfigSection("Velocity").getDouble("X-Multiplier")
				, e.getPlayer().getVelocity().getY()  * ConfigUtils.getConfigSection("Velocity").getDouble("Y-Multiplier")
				, e.getPlayer().getVelocity().getZ() * ConfigUtils.getConfigSection("Velocity").getDouble("Z-Multiplier")));
	}

}
