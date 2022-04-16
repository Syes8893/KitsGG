package me.syes.kits.arena;

import java.util.Random;

import me.syes.kits.utils.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Arena {
	
	public int minX, maxX, minY, maxY, minZ, maxZ;
	public World world;
	public Location lobbySpawn;
	
	public Arena(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, String worldName, Location lobbySpawn) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
		if(worldName != null && Bukkit.getWorld(worldName) != null)
			this.world = Bukkit.getWorld(worldName);
		else this.world = Bukkit.getWorlds().get(0);
		if(lobbySpawn != null)
			this.lobbySpawn = lobbySpawn;
		else this.lobbySpawn = new Location(this.world, 0, 100, 0, 0, 90);
	}

	@Deprecated
	public void setMinBounds(int minX, int minY, int minZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
	}


	public Location getMinBounds() {
		return new Location(this.world, this.minX, this.minY, this.minZ);
	}

	@Deprecated
	public void setMaxBounds(int maxX, int maxY, int maxZ) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public Location getMaxBounds() {
		return new Location(this.world, this.maxX, this.maxY, this.maxZ);
	}
	
	public Location getCenter() {
		return new Location(this.world, (this.maxX + this.minX)/2, (this.maxY + this.minY)/2, (this.maxZ + this.minZ)/2);
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void setLobbySpawn(Location lobbySpawn) {
		this.lobbySpawn = lobbySpawn;
	}
	
	public Location getLobbySpawn() {
		return this.lobbySpawn;
	}
	
	public Location getRandomSpawn() {
		int x = (int) ((new Random().nextDouble() * this.maxX * 2) + this.minX);
		int z = (int) ((new Random().nextDouble() * this.maxZ * 2) + this.minZ);
		int y = ArenaUtils.calculateLowestBlock(this.getWorld(), x, z , minY, maxY);
//		while(this.world.getBlockAt(x, y, z).getType().equals(Material.AIR)){
//			y += -1;
//		}
		return new Location(this.world, x, y + 1.5, z);
	}

}
