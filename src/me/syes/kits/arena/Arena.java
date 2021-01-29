package me.syes.kits.arena;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Arena {
	
	public int minX, maxX, minZ, maxZ;
	public World world;
	public Location lobbySpawn;
	
	public Arena(int minX, int maxX, int minZ, int maxZ, String worldName, Location lobbySpawn) {
		this.minX = minX;
		this.maxX = maxX;
		this.minZ = minZ;
		this.maxZ = maxZ;
		if(worldName != null && Bukkit.getWorld(worldName) != null)
			this.world = Bukkit.getWorld(worldName);
		else this.world = Bukkit.getWorlds().get(0);
		if(lobbySpawn != null)
			this.lobbySpawn = lobbySpawn;
		else this.lobbySpawn = new Location(this.world, 0, 100, 0, 0, 90);
	}
	
	public void setMinBounds(int minX, int minZ) {
		this.minX = minX;
		this.minZ = minZ;
	}
	
	public Location getMinBounds() {
		return new Location(this.world, this.minX, this.world.getHighestBlockYAt(this.minX, this.minZ), this.minZ);
	}
	
	public void setMaxBounds(int maxX, int maxZ) {
		this.maxX = maxX;
		this.maxZ = maxZ;
	}
	
	public Location getMaxBounds() {
		return new Location(this.world, this.maxX, this.world.getHighestBlockYAt(this.maxX, this.maxZ), this.maxZ);
	}
	
	public Location getCenter() {
		return new Location(this.world, (this.maxX + this.minX)/2, this.world.getHighestBlockYAt((this.maxX + this.minX)/2, (this.maxZ + this.minZ)/2), (this.maxZ + this.minZ)/2);
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
		int y = this.world.getHighestBlockYAt(x, z);
		while(this.world.getBlockAt(x, y, z).getType().equals(Material.AIR)){
			y += -1;
		}
		return new Location(this.world, x, y + 1.5, z);
	}

}
