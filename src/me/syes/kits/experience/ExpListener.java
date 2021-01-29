package me.syes.kits.experience;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.syes.kits.Kits;
import me.syes.kits.bukkitevents.ExpChangeEvent;
import me.syes.kits.kit.Kit;
import me.syes.kits.kitplayer.KitPlayer;
import me.syes.kits.utils.MessageUtils;

public class ExpListener implements Listener{
	
	@EventHandler
	public void onExpChange(ExpChangeEvent e) {
		KitPlayer kp = e.getKitPlayer();
		Player p = e.getPlayer();
		ExpManager expManager = Kits.getInstance().getExpManager();
		for(Kit k : Kits.getInstance().getKitManager().getKits()) {
			if(kp.getExp() + e.getExpChanged() >= k.getRequiredExp() && k.getRequiredExp() > 0 && !kp.getUnlockedKits().contains(k.getName())) {
				kp.addUnlockedKit(k.getName());
				p.sendMessage("§7§m----------------------------------------");
				p.sendMessage("§aCongratulations! §fYou have unlocked the §a" + k.getName() + " §fKit");
				p.sendMessage("§7§m----------------------------------------");
				MessageUtils.sendTitle(p, "&a&lKIT UNLOCKED!", " &fUnlocked the &a" + k.getName() + " &fKit");
			}
		}
		if(kp.getExp() + e.getExpChanged() >= expManager.getExpForNextLevel(kp) && expManager.getExpForNextLevel(kp) != 0) {
			p.sendMessage("§7§m----------------------------------------");
			p.sendMessage("§aCongratulations! §fYou reached Level §r" + expManager.getLevel(kp.getExp() + e.getExpChanged()).getPrefix());
			p.sendMessage("§7§m----------------------------------------");
			MessageUtils.sendTitle(p, "&a&lLEVEL UP!", expManager.getLevel(kp.getExp()).getPrefix() + " &7>> " + expManager.getLevel(kp.getExp() + e.getExpChanged()).getPrefix());
			p.setPlayerListName(expManager.getLevel(kp.getExp() + e.getExpChanged()).getPrefix()
					+ p.getPlayerListName().replace(expManager.getLevel(kp.getExp()).getPrefix(), ""));
			//TODO make tabname change upon Level-up
		}
	}

}
