package io.github.hsyyid.ultimategames.arenas;

import com.dracade.ember.core.Arena;
import com.dracade.ember.core.SpawnPoint;

public class DeathmatchArena extends Arena
{
	private SpawnPoint teamASpawn;
	private SpawnPoint teamBSpawn;
	
	public DeathmatchArena(String name, SpawnPoint spawn)
	{
		super(name, spawn);
	}
	
	public void setTeamASpawn(SpawnPoint teamASpawn)
	{
		this.teamASpawn = teamASpawn;
	}
	
	public void setTeamBSpawn(SpawnPoint teamBSpawn)
	{
		this.teamBSpawn = teamBSpawn;
	}
	
	public SpawnPoint getTeamASpawn()
	{
		return teamASpawn;
	}
	
	public SpawnPoint getTeamBSpawn()
	{
		return teamBSpawn;
	}
}
