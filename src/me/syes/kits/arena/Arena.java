package me.syes.kits.arena;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.syes.kits.utils.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Arena {
	
	public int[] xValues, yValues, zValues;
	public World world;
	public Location lobbySpawn;
	
	public Arena(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, String worldName, Location lobbySpawn) {
		this.xValues = new int[2];
		this.yValues = new int[2];
		this.zValues = new int[2];
		if(worldName != null && Bukkit.getWorld(worldName) != null)
			this.world = Bukkit.getWorld(worldName);
		else this.world = Bukkit.getWorlds().get(0);
		if(lobbySpawn != null)
			this.lobbySpawn = lobbySpawn;
		else this.lobbySpawn = new Location(this.world, 0, 100, 0, 0, 90);
	}

	@Deprecated
	public void setMinBounds(int minX, int minY, int minZ) {
		this.xValues[0] = minX;
		this.yValues[0] = minY;
		this.zValues[0] = minZ;
		sortValues();
	}


	public Location getMinBounds() {
		return new Location(this.world, this.xValues[0], this.yValues[0], this.zValues[0]);
	}

	@Deprecated
	public void setMaxBounds(int maxX, int maxY, int maxZ) {
		this.xValues[1] = maxX;
		this.yValues[1] = maxY;
		this.zValues[1] = maxZ;
		sortValues();
	}

	public void sortValues(){
		Arrays.sort(xValues);
		Arrays.sort(yValues);
		Arrays.sort(zValues);
	}
	
	public Location getMaxBounds() {
		return new Location(this.world, this.xValues[1], this.yValues[1], this.zValues[1]);
	}
	
	public Location getCenter() {
		return new Location(this.world, (this.xValues[1] + this.xValues[0])/2, (this.yValues[1] + this.yValues[0])/2, (this.zValues[1] + this.zValues[1])/2);
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
		int x = (int) (new Random().nextDouble() *(getMaxBounds().getBlockX() - getCenter().getBlockX())*2) + (getMinBounds().getBlockX() - getCenter().getBlockX());
		int z = (int) (new Random().nextDouble() *(getMaxBounds().getBlockX() - getCenter().getBlockX())*2) + (getMinBounds().getBlockX() - getCenter().getBlockX());
		int y = ArenaUtils.calculateLowestBlock(this.getWorld(), x, z , yValues[0], yValues[1]);
//		while(this.world.getBlockAt(x, y, z).getType().equals(Material.AIR)){
//			y += -1;
//		}
		return new Location(this.world, x, y + 1.5, z);
	}

}
