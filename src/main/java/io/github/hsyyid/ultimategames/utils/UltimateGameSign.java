package io.github.hsyyid.ultimategames.utils;

import com.google.common.collect.Lists;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UltimateGameSign
{
	private Location<World> location;
	private String arena;
	private String uuid;
	
	public List<String> teamA;
	public List<String> teamB;

	public UltimateGameSign(Location<World> location, String arena)
	{
		this.location = location;
		this.arena = arena;
		this.uuid = UUID.randomUUID().toString();
		this.teamA = Lists.newArrayList();
		this.teamB = Lists.newArrayList();
	}

	public String getArena()
	{
		return arena;
	}

	public Location<World> getLocation()
	{
		return location;
	}

	public void setArena(String arena)
	{
		this.arena = arena;
	}

	public void setLocation(Location<World> location)
	{
		this.location = location;
	}

	public List<String> getTeamA()
	{
		return teamA;
	}

	public List<String> getTeamB()
	{
		return teamB;
	}

	public Collection<UUID> players()
	{
		Collection<UUID> allPlayers = Lists.newArrayList();

		for (String player : this.teamA)
		{
			allPlayers.add(UUID.fromString(player));
		}

		for (String player : this.teamB)
		{
			allPlayers.add(UUID.fromString(player));
		}

		return allPlayers;
	}
	
	public String getUuid()
	{
		return uuid;
	}
}
