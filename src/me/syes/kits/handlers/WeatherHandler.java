package me.syes.kits.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.syes.kits.utils.ConfigUtils;

public class WeatherHandler implements Listener{
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if(ConfigUtils.getConfigSection("Extra").getBoolean("Disable-Weather"))
			e.setCancelled(true);
	}

}
