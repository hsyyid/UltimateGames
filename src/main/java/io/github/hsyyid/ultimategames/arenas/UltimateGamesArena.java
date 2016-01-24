package io.github.hsyyid.ultimategames.arenas;

import com.dracade.ember.core.Arena;
import com.dracade.ember.core.SpawnPoint;

public class UltimateGamesArena extends Arena
{
	private SpawnPoint teamASpawn;
	private SpawnPoint teamBSpawn;
	private SpawnPoint spectatorSpawn;
	private String teamALoadout;
	private String teamBLoadout;
	private int teamSize;
	private int length;

	public UltimateGamesArena(String name, SpawnPoint spawn, int teamSize, int length)
	{
		super(name, spawn);
		this.teamSize = teamSize;
		this.length = length;
	}

	public void setTeamASpawn(SpawnPoint teamASpawn)
	{
		this.teamASpawn = teamASpawn;
	}

	public void setTeamALoadout(String teamALoadout)
	{
		this.teamALoadout = teamALoadout;
	}

	public void setTeamBSpawn(SpawnPoint teamBSpawn)
	{
		this.teamBSpawn = teamBSpawn;
	}
	
	public void setSpectatorSpawn(SpawnPoint spectatorSpawn)
	{
		this.spectatorSpawn = spectatorSpawn;
	}

	public void setTeamBLoadout(String teamBLoadout)
	{
		this.teamBLoadout = teamBLoadout;
	}

	public void setTeamSize(int teamSize)
	{
		this.teamSize = teamSize;
	}
	
	public void setLength(int length)
	{
		this.length = length;
	}

	public SpawnPoint getTeamASpawn()
	{
		return teamASpawn;
	}

	public String getTeamALoadout()
	{
		return teamALoadout;
	}

	public SpawnPoint getTeamBSpawn()
	{
		return teamBSpawn;
	}
	
	public SpawnPoint getSpectatorSpawn()
	{
		return spectatorSpawn;
	}

	public String getTeamBLoadout()
	{
		return teamBLoadout;
	}

	public int getTeamSize()
	{
		return teamSize;
	}
	
	public int getLength()
	{
		return length;
	}
}
