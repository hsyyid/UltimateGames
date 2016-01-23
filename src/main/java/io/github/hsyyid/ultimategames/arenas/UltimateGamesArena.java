package io.github.hsyyid.ultimategames.arenas;

import com.dracade.ember.core.Arena;
import com.dracade.ember.core.SpawnPoint;

public class UltimateGamesArena extends Arena
{
	private SpawnPoint teamASpawn;
	private SpawnPoint teamBSpawn;
	private String teamALoadout;
	private String teamBLoadout;
	private int teamSize;

	public UltimateGamesArena(String name, SpawnPoint spawn, int teamSize)
	{
		super(name, spawn);
		this.teamSize = teamSize;
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

	public void setTeamBLoadout(String teamBLoadout)
	{
		this.teamBLoadout = teamBLoadout;
	}

	public void setTeamSize(int teamSize)
	{
		this.teamSize = teamSize;
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

	public String getTeamBLoadout()
	{
		return teamBLoadout;
	}

	public int getTeamSize()
	{
		return teamSize;
	}
}
