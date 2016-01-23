package io.github.hsyyid.ultimategames.arenas;

import com.dracade.ember.core.Arena;
import com.dracade.ember.core.SpawnPoint;

public class DeathmatchArena extends Arena
{
	private SpawnPoint teamASpawn;
	private SpawnPoint teamBSpawn;
	private String teamALoadout;
	private String teamBLoadout;

	public DeathmatchArena(String name, SpawnPoint spawn)
	{
		super(name, spawn);
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
}
