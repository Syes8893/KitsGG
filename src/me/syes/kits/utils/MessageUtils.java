package me.syes.kits.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageUtils {
	
	public static void broadcastTitle(String title, String subtitle) {
		for(Player pl : Bukkit.getOnlinePlayers()) {
			pl.resetTitle();
			pl.sendTitle(title.replace("&", "§"), subtitle.replace("&", "§"));
		}
	}
	
	public static void sendTitle(Player pl, String title, String subtitle) {
		pl.resetTitle();
		pl.sendTitle(title.replace("&", "§"), subtitle.replace("&", "§"));
	}
	
	public static void broadcastMessage(String msg) {
		for(Player pl : Bukkit.getOnlinePlayers()) {
			pl.sendMessage(msg.replace("&", "§"));
		}
	}
	
}
