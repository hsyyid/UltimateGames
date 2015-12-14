package io.github.hsyyid.ultimategames.minigames;

import com.dracade.ember.Ember;
import com.dracade.ember.core.Minigame;
import com.dracade.ember.core.events.minigame.MinigameStartedEvent;
import com.dracade.ember.core.events.minigame.MinigameStoppedEvent;
import com.google.common.collect.Lists;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.DeathmatchArena;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeathmatchMinigame implements Minigame
{
	private List<Player> teamA;
	private List<Player> teamB;
	private DeathmatchArena arena;
	private int teamAPoints;
	private int teamBPoints;

	/**
	 * Creates a new DeathmatchMinigame instance.
	 *
	 * @param arena the arena to be played.
	 * @param teamA the players on team A.
	 * @param teamB the players on team B.
	 * @throws Exception If the match failed to register.
	 */
	public DeathmatchMinigame(DeathmatchArena arena, List<Player> teamA, List<Player> teamB) throws Exception
	{
		this.teamA = teamA;
		this.teamB = teamB;
		this.arena = arena;

		Ember.register(arena, this);
	}

	@Override
	public long delay()
	{
		return 0;
	}

	@Override
	public boolean events()
	{
		return true;
	}

	@Override
	public long interval()
	{
		return 0;
	}

	@Override
	public Collection<Player> players()
	{
		Collection<Player> allPlayers = Lists.newArrayList();

		for (Player player : this.teamA)
		{
			allPlayers.add(player);
		}

		for (Player player : this.teamB)
		{
			allPlayers.add(player);
		}

		return allPlayers;
	}

	@Listener
	public void onStart(MinigameStartedEvent event)
	{
		if (event.getMinigame().equals(this))
		{
			for (Player player : players())
			{
				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Deathmatch starting..."));
			}

			for (Player player : this.teamA)
			{
				if (player.getWorld().getUniqueId().equals(this.arena.getTeamASpawn().getLocation().getExtent().getUniqueId()))
				{
					player.setLocation(this.arena.getTeamASpawn().getLocation());
				}
				else
				{
					player.transferToWorld(this.arena.getTeamASpawn().getLocation().getExtent().getUniqueId(), this.arena.getTeamASpawn().getLocation().getPosition());
				}

				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GOLD, "May the games begin!"));
			}

			for (Player player : this.teamB)
			{
				if (player.getWorld().getUniqueId().equals(this.arena.getTeamBSpawn().getLocation().getExtent().getUniqueId()))
				{
					player.setLocation(this.arena.getTeamBSpawn().getLocation());
				}
				else
				{
					player.transferToWorld(this.arena.getTeamBSpawn().getLocation().getExtent().getUniqueId(), this.arena.getTeamBSpawn().getLocation().getPosition());
				}

				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GOLD, "May the games begin!"));
			}

			Scheduler scheduler = UltimateGames.game.getScheduler();
			Task.Builder taskBuilder = scheduler.createTaskBuilder();

			taskBuilder.execute(() -> {
				try
				{
					Ember.unregister(this.arena);
				}
				catch (Exception e)
				{
					System.out.println("[UltimateGames]: Error when ending deathmatch in arena " + arena.getName());
				}
			}).delay(1, TimeUnit.MINUTES).name("UltimateGames - End Deathmatch").submit(UltimateGames.game.getPluginManager().getPlugin("UltimateGames").get().getInstance().get());
		}
	}

	@Listener
	public void onStopped(MinigameStoppedEvent event)
	{
		if (event.getMinigame().equals(this))
		{
			for (Player player : players())
			{
				if (this.teamAPoints > this.teamBPoints)
					player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Team A has won the Deathmatch!"));
				else if (this.teamBPoints > this.teamAPoints)
					player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Team B has won the Deathmatch!"));
				else
					player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GRAY, "The deathmatch has ended with a draw!"));

				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Deathmatch has ended!"));
				
				if (player.getWorld().getUniqueId().equals(this.arena.getSpawn().getLocation().getExtent().getUniqueId()))
				{
					player.setLocation(this.arena.getSpawn().getLocation());
				}
				else
				{
					player.transferToWorld(this.arena.getSpawn().getLocation().getExtent().getUniqueId(), this.arena.getSpawn().getLocation().getPosition());
				}
				
				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Teleported back to lobby."));
			}
		}
	}

	@Override
	public void accept(Task t)
	{
		;
	}

	@Listener
	public void onPlayerKilled(DestructEntityEvent.Death event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = event.getCause().first(Player.class).get();
			Living entityTarget = event.getTargetEntity();

			if (entityTarget instanceof Player)
			{
				Player target = (Player) entityTarget;

				if (this.teamA.contains(player) && this.teamB.contains(target))
				{
					this.teamAPoints++;
				}
				else if (this.teamB.contains(player) && this.teamA.contains(target))
				{
					this.teamBPoints++;
				}
			}
		}
	}

	@Listener
	public void onPlayerDamaged(DamageEntityEvent event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = event.getCause().first(Player.class).get();
			Entity entityTarget = event.getTargetEntity();

			if (entityTarget instanceof Player)
			{
				Player target = (Player) entityTarget;

				if ((teamA.contains(player) && teamA.contains(target)) || (teamB.contains(player) && teamB.contains(target)))
				{
					player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "You cannot damage players on your team!"));
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Listener
	public void onPlayerRespawn(RespawnPlayerEvent event)
	{
		Player player = event.getTargetEntity();
		
		if (this.teamA.contains(player))
		{
			event.setToTransform(event.getToTransform().setLocation(this.arena.getTeamASpawn().getLocation()));
		}
		else if(this.teamB.contains(player))
		{
			event.setToTransform(event.getToTransform().setLocation(this.arena.getTeamBSpawn().getLocation()));
		}
	}
}
