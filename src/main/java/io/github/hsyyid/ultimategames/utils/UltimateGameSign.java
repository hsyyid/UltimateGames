package io.github.hsyyid.ultimategames.utils;

import com.dracade.ember.core.Arena;
import com.google.common.collect.Lists;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UltimateGameSign
{
	private Location<World> location;
	private Arena arena;
	
	public List<UUID> teamA;
	public List<UUID> teamB;
	
	public UltimateGameSign(Location<World> location, Arena arena)
	{
		this.location = location;
		this.arena = arena;
	}
	
	public Arena getArena()
	{
		return arena;
	}
	
	public Location<World> getLocation()
	{
		return location;
	}
	
	public void setArena(Arena arena)
	{
		this.arena = arena;
	}
	
	public void setLocation(Location<World> location)
	{
		this.location = location;
	}
	
	public List<UUID> getTeamA()
	{
		return teamA;
	}
	
	public List<UUID> getTeamB()
	{
		return teamB;
	}
	
	public Collection<UUID> players()
	{
		Collection<UUID> allPlayers = Lists.newArrayList();

		for (UUID player : this.teamA)
		{
			allPlayers.add(player);
		}

		for (UUID player : this.teamB)
		{
			allPlayers.add(player);
		}

		return allPlayers;
	}
}
