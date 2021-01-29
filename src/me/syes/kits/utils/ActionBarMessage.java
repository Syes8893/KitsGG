package me.syes.kits.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBarMessage {
  
   public static void sendMessage(Player p, String message) {
	   PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
	   ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
   }
   
   public static void broadcastMessage(String message) {
	   PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
	   for (Player p : Bukkit.getServer().getOnlinePlayers()) {
	   ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	   }
   }
  
}
