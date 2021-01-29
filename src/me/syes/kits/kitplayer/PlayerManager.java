package me.syes.kits.kitplayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

	public HashMap<UUID, KitPlayer> kitPlayers;
	
	public PlayerManager() {
		kitPlayers = new HashMap<UUID, KitPlayer>();
	}

	public HashMap<UUID, KitPlayer> getKitPlayers() {
		return kitPlayers;
	}

	public void addKitPlayers(KitPlayer kitPlayer) {
		this.kitPlayers.put(kitPlayer.getUuid(), kitPlayer);
	}
	
	public KitPlayer getKitPlayer(UUID uuid) {
		return this.kitPlayers.get(uuid);
	}
	
}
