package io.github.hsyyid.ultimategames.utils;

import com.dracade.ember.Ember;
import com.google.common.collect.Lists;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.UltimateGamesArena;
import io.github.hsyyid.ultimategames.minigames.DeathmatchMinigame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Utils
{
	public static Optional<UltimateGamesArena> getArena(String arena)
	{
		for (UltimateGamesArena dmArena : UltimateGames.arenas)
		{
			if (dmArena.getName().equalsIgnoreCase(arena))
			{
				return Optional.of(dmArena);
			}
		}

		return Optional.empty();
	}

	public static void startSignService(UltimateGameSign gameSign, Location<World> signLocation, String arenaName)
	{
		Scheduler scheduler = UltimateGames.game.getScheduler();
		Task.Builder taskBuilder = scheduler.createTaskBuilder();

		taskBuilder.execute(() -> {
			int teamASize = gameSign.getTeamA().size();
			int teamBSize = gameSign.getTeamB().size();

			if (signLocation.getTileEntity().isPresent())
			{
				TileEntity tileEntity = signLocation.getTileEntity().get();
				Optional<SignData> optionalSignData = tileEntity.getOrCreate(SignData.class);

				if (optionalSignData.isPresent())
				{
					SignData data = optionalSignData.get();
					data = data.set(data.getValue(Keys.SIGN_LINES).get().set(2, Text.of(TextColors.BLUE, "Team A: ", TextColors.GRAY, teamASize)));
					data = data.set(data.getValue(Keys.SIGN_LINES).get().set(3, Text.of(TextColors.RED, "Team B: ", TextColors.GRAY, teamBSize)));
					tileEntity.offer(data);
				}

				if (Utils.getArena(arenaName).isPresent() && !Ember.getMinigame(Utils.getArena(arenaName).get()).isPresent())
				{
					UltimateGamesArena dmArena = Utils.getArena(arenaName).get();

					if (gameSign.getTeamA().size() == dmArena.getTeamSize() && gameSign.getTeamA().size() == gameSign.getTeamB().size())
					{
						List<Player> teamA = Lists.newArrayList();
						List<Player> teamB = Lists.newArrayList();

						for (Player p : UltimateGames.game.getServer().getOnlinePlayers())
						{
							if (gameSign.getTeamA().contains(p.getUniqueId().toString()))
							{
								teamA.add(p);
							}
						}

						for (Player p : UltimateGames.game.getServer().getOnlinePlayers())
						{
							if (gameSign.getTeamB().contains(p.getUniqueId().toString()))
							{
								teamB.add(p);
							}
						}

						try
						{
							new DeathmatchMinigame(dmArena, teamA, teamB);
						}
						catch (Exception e)
						{
							;
						}

						gameSign.getTeamA().clear();
						gameSign.getTeamB().clear();
					}
				}
				else if (!Utils.getArena(arenaName).isPresent())
				{
					for (Task task : Sponge.getScheduler().getTasksByName("UltimateGames - Update UltimateGamesSign " + gameSign.getUuid()))
					{
						task.cancel();
					}
				}
			}
			else
			{
				for (Task task : Sponge.getScheduler().getTasksByName("UltimateGames - Update UltimateGamesSign " + gameSign.getUuid()))
				{
					task.cancel();
				}
			}
		}).interval(1, TimeUnit.MILLISECONDS).name("UltimateGames - Update UltimateGamesSign " + gameSign.getUuid()).submit(UltimateGames.game.getPluginManager().getPlugin("UltimateGames").get().getInstance().get());
	}
}
