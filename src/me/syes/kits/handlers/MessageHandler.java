package me.syes.kits.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.syes.kits.Kits;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.ConfigUtils;

public class MessageHandler implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(!ConfigUtils.getConfigSection("Messages").getBoolean("Chat-Prefix"))
			return;
		Player p = e.getPlayer();
		KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		if(ConfigUtils.getConfigSection("Messages").getBoolean("Handle-Chat"))
			e.setFormat(Kits.getInstance().getExpManager().getPlayerLevel(kitPlayer).getNameColor() + p.getName() + ": §r" + e.getMessage());
		//TODO - temporary chat format
		if(p.hasPermission("kits.colorchat")) e.setMessage(e.getMessage().replace("&", "§"));
		e.setFormat(Kits.getInstance().expManager.getPlayerLevel(kitPlayer).getPrefix() + e.getFormat());
		//e.setFormat("§2[" + kitPlayer.getKills() + "] §r" + e.getFormat());
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerDeathByPlayer(PlayerDeathEvent e) {
		if(!ConfigUtils.getConfigSection("Messages").getBoolean("Death-Message-Prefix"))
			return;
		e.setDeathMessage(e.getDeathMessage().replace("[", "").replace("]", ""));
		Player p = e.getEntity();
		KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
		//e.setDeathMessage(e.getDeathMessage().replace(p.getName(), "§2[" + kitPlayer.getKills() + "] §a" + p.getName() + "§f"));
		e.setDeathMessage(e.getDeathMessage().replaceFirst(p.getName() + " ", Kits.getInstance().expManager.getPlayerLevel(kitPlayer).getPrefix()
				+ Kits.getInstance().expManager.getPlayerLevel(kitPlayer).getNameColor() + p.getName() + " " + "§f"));
		Player k = e.getEntity().getKiller();
		if(k != null && k != p) {
			KitPlayer killerKitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(k.getUniqueId());
			//e.setDeathMessage(e.getDeathMessage().replace(k.getName(), "§2[" + killerKitPlayer.getKills() + "] §a" + k.getName() + "§f"));
			e.setDeathMessage(e.getDeathMessage().replaceFirst(k.getName(), Kits.getInstance().expManager.getPlayerLevel(killerKitPlayer).getPrefix()
					+ Kits.getInstance().expManager.getPlayerLevel(killerKitPlayer).getNameColor() + k.getName() + "§f"));
		}
	}
//
//	@EventHandler (priority = EventPriority.MONITOR)
//	public void onPlayerDeath(PlayerDeathEvent e){
//		if(!ConfigUtils.getConfigSection("Messages").getBoolean("Death-Message-Prefix"))
//			return;
//		if(e.getEntity().getKiller() == null)
//			return;
//		if(!(e.getEntity() instanceof Player))
//			return;
//		e.setDeathMessage(e.getDeathMessage().replace("[", "").replace("]", ""));
//		Player p = e.getEntity();
//		KitPlayer kitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(p.getUniqueId());
//		e.setDeathMessage(e.getDeathMessage().replaceFirst(p.getName() + " ", Kits.getInstance().expManager.getPlayerLevel(kitPlayer).getPrefix()
//				+ Kits.getInstance().expManager.getPlayerLevel(kitPlayer).getNameColor() + p.getName() + " " + "§f"));
//		Player k = e.getEntity().getKiller();
//		if(k != null && k != p) {
//			KitPlayer killerKitPlayer = Kits.getInstance().playerManager.getKitPlayers().get(k.getUniqueId());
//			e.setDeathMessage(e.getDeathMessage().replaceFirst(k.getName(), Kits.getInstance().expManager.getPlayerLevel(killerKitPlayer).getPrefix()
//					+ Kits.getInstance().expManager.getPlayerLevel(killerKitPlayer).getNameColor() + k.getName() + "§f"));
//		}
//	}

}
