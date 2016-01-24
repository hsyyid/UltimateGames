package io.github.hsyyid.ultimategames.minigames;

import com.dracade.ember.Ember;
import com.dracade.ember.core.Minigame;
import com.dracade.ember.core.events.minigame.MinigameStartedEvent;
import com.dracade.ember.core.events.minigame.MinigameStoppedEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.UltimateGamesArena;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibilities;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeathmatchMinigame implements Minigame
{
	private List<Player> teamA;
	private List<Player> teamB;
	private UltimateGamesArena arena;
	private int teamAPoints;
	private int teamBPoints;

	private Scoreboard scoreboard;
	private Team teamAScoreboardTeam;
	private Team teamBScoreboardTeam;

	/**
	 * Creates a new DeathmatchMinigame instance.
	 *
	 * @param arena the arena to be played.
	 * @param teamA the players on team A.
	 * @param teamB the players on team B.
	 * @throws Exception If the match failed to register.
	 */
	public DeathmatchMinigame(UltimateGamesArena arena, List<Player> teamA, List<Player> teamB) throws Exception
	{
		this.teamA = teamA;
		this.teamB = teamB;
		this.arena = arena;

		Objective.Builder objectiveBuilder = Sponge.getRegistry().createBuilder(Objective.Builder.class);

		Text title = Text.of(TextColors.AQUA, "UltimateGames");
		final Objective mainObjective = objectiveBuilder.name("<Arena>").criterion(Criteria.DUMMY).displayName(title).build();

		Score teamAName = mainObjective.getOrCreateScore(Text.of(TextColors.BLUE, TextStyles.BOLD, "Team A"));
		teamAName.setScore(4);

		this.teamAScoreboardTeam = Team.builder()
			.name("TeamA")
			.displayName(Text.of("TeamA"))
			.prefix(Text.of(TextColors.BLUE))
			.nameTagVisibility(Visibilities.ALL)
			.canSeeFriendlyInvisibles(true)
			.allowFriendlyFire(false)
			.members(Sets.newHashSet(Text.of(TextColors.BLUE, TextStyles.BOLD, "Team A")))
			.build();

		Score teamAScore = mainObjective.getOrCreateScore(Text.of(TextColors.BLUE, "Kills: "));
		teamAScore.setScore(3);

		Team teamAScoreTeam = Team.builder()
			.name("TeamAScore")
			.displayName(Text.of("TeamAScore"))
			.members(Sets.newHashSet(Text.of(TextColors.BLUE, "Kills: ")))
			.build();

		Score teamBName = mainObjective.getOrCreateScore(Text.of(TextColors.RED, TextStyles.BOLD, "Team B"));
		teamBName.setScore(2);

		this.teamBScoreboardTeam = Team.builder()
			.name("TeamB")
			.displayName(Text.of("TeamB"))
			.prefix(Text.of(TextColors.RED))
			.nameTagVisibility(Visibilities.ALL)
			.canSeeFriendlyInvisibles(true)
			.allowFriendlyFire(false)
			.members(Sets.newHashSet(Text.of(TextColors.RED, TextStyles.BOLD, "Team B")))
			.build();

		Score teamBScore = mainObjective.getOrCreateScore(Text.of(TextColors.RED, "Kills: "));
		teamBScore.setScore(1);

		Team teamBScoreTeam = Team.builder()
			.name("TeamBScore")
			.displayName(Text.of("TeamBScore"))
			.members(Sets.newHashSet(Text.of(TextColors.RED, "Kills: ")))
			.build();

		List<Objective> objectives = new ArrayList<Objective>();
		objectives.add(mainObjective);

		scoreboard = Scoreboard.builder().objectives(objectives).build();
		scoreboard.registerTeam(this.teamAScoreboardTeam);
		scoreboard.registerTeam(teamAScoreTeam);
		scoreboard.registerTeam(this.teamBScoreboardTeam);
		scoreboard.registerTeam(teamBScoreTeam);
		scoreboard.updateDisplaySlot(mainObjective, DisplaySlots.SIDEBAR);

		Scheduler scheduler = Sponge.getScheduler();

		scheduler.createTaskBuilder().execute(() -> {
			try
			{
				teamAScoreTeam.setSuffix(Text.of(TextColors.GRAY, teamAPoints));
				teamBScoreTeam.setSuffix(Text.of(TextColors.GRAY, teamBPoints));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}).interval(1, TimeUnit.MILLISECONDS).name("UltimateGames - Update scoreboard").submit(Sponge.getPluginManager().getPlugin("UltimateGames").get().getInstance().get());

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
				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Deathmatch starting..."));
			}

			for (Player player : this.teamA)
			{
				player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
				player.setScoreboard(scoreboard);
				this.teamAScoreboardTeam.addMember(player.getTeamRepresentation());

				if (this.arena.getTeamALoadout() != null)
					UltimateGames.game.getCommandManager().process(Sponge.getServer().getConsole(), "kit " + this.arena.getTeamALoadout() + " " + player.getName());

				if (player.getWorld().getUniqueId().equals(this.arena.getTeamASpawn().getLocation().getExtent().getUniqueId()))
				{
					player.setLocation(this.arena.getTeamASpawn().getLocation());
				}
				else
				{
					player.transferToWorld(this.arena.getTeamASpawn().getLocation().getExtent().getUniqueId(), this.arena.getTeamASpawn().getLocation().getPosition());
				}

				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GOLD, "May the games begin!"));
			}

			for (Player player : this.teamB)
			{
				player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
				player.setScoreboard(scoreboard);
				this.teamBScoreboardTeam.addMember(player.getTeamRepresentation());

				if (this.arena.getTeamBLoadout() != null)
					UltimateGames.game.getCommandManager().process(Sponge.getServer().getConsole(), "kit " + this.arena.getTeamBLoadout() + " " + player.getName());

				if (player.getWorld().getUniqueId().equals(this.arena.getTeamBSpawn().getLocation().getExtent().getUniqueId()))
				{
					player.setLocation(this.arena.getTeamBSpawn().getLocation());
				}
				else
				{
					player.transferToWorld(this.arena.getTeamBSpawn().getLocation().getExtent().getUniqueId(), this.arena.getTeamBSpawn().getLocation().getPosition());
				}

				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GOLD, "May the games begin!"));
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
				player.setScoreboard(null);

				if (this.teamAPoints > this.teamBPoints)
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Team A has won the Deathmatch!"));
				else if (this.teamBPoints > this.teamAPoints)
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Team B has won the Deathmatch!"));
				else
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GRAY, "The deathmatch has ended with a draw!"));

				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Deathmatch has ended!"));

				if (player.getWorld().getUniqueId().equals(this.arena.getSpawn().getLocation().getExtent().getUniqueId()))
				{
					player.setLocation(this.arena.getSpawn().getLocation());
				}
				else
				{
					player.transferToWorld(this.arena.getSpawn().getLocation().getExtent().getUniqueId(), this.arena.getSpawn().getLocation().getPosition());
				}

				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Teleported back to lobby."));
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
		if (event.getCause().first(DamageSource.class).isPresent())
		{
			DamageSource dmgSource = event.getCause().first(DamageSource.class).get();

			if (dmgSource instanceof EntityDamageSource)
			{
				EntityDamageSource entityDmgSource = (EntityDamageSource) dmgSource;
				Entity entity = entityDmgSource.getSource();
				Living entityTarget = event.getTargetEntity();

				if (entity instanceof Player && entityTarget instanceof Player)
				{
					Player player = (Player) entity;
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
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "You cannot damage players on your team!"));
					event.setCancelled(true);
				}
			}
		}
	}

	public void onPlayerRespawn(RespawnPlayerEvent event)
	{
		Player player = event.getTargetEntity();

		if (this.teamA.contains(player))
		{
			event.setToTransform(event.getToTransform().setLocation(this.arena.getTeamASpawn().getLocation()));
		}
		else if (this.teamB.contains(player))
		{
			event.setToTransform(event.getToTransform().setLocation(this.arena.getTeamBSpawn().getLocation()));
		}
	}

	@Listener
	public void onPlayerSpawn(SpawnEntityEvent event, @First Player player)
	{
		if (this.teamA.contains(player))
		{
			if (this.arena.getTeamALoadout() != null)
				UltimateGames.game.getCommandManager().process(Sponge.getServer().getConsole(), "kit " + this.arena.getTeamALoadout() + " " + player.getName());
		}
		else if (this.teamB.contains(player))
		{
			if (this.arena.getTeamBLoadout() != null)
				UltimateGames.game.getCommandManager().process(Sponge.getServer().getConsole(), "kit " + this.arena.getTeamBLoadout() + " " + player.getName());
		}
	}

	@Listener
	public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event)
	{
		if (players().contains(event.getTargetEntity()))
		{
			for (Player player : this.teamA)
			{
				if (player.getUniqueId().equals(event.getTargetEntity().getUniqueId()))
				{
					this.teamA.remove(event.getTargetEntity().getUniqueId());
					event.getTargetEntity().setLocation(this.arena.getSpawn().getLocation());
				}
			}

			for (Player player : this.teamB)
			{
				if (player.getUniqueId().equals(event.getTargetEntity().getUniqueId()))
				{
					this.teamB.remove(event.getTargetEntity().getUniqueId());
					event.getTargetEntity().setLocation(this.arena.getSpawn().getLocation());
				}
			}
		}
	}
}
