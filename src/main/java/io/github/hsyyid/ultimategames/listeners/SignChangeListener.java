package io.github.hsyyid.ultimategames.listeners;

import com.dracade.ember.Ember;
import com.dracade.ember.core.Arena;
import com.google.common.collect.Lists;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.DeathmatchArena;
import io.github.hsyyid.ultimategames.minigames.DeathmatchMinigame;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SignChangeListener
{
	@Listener
	public void onSignChange(ChangeSignEvent event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			Sign sign = event.getTargetTile();
			final Location<World> signLocation = sign.getLocation();
			SignData signData = event.getText();
			String line0 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(0));
			String line1 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(1));

			if (line0.equals("[UltimateGames]"))
			{
				if (player.hasPermission("ultimategames.signs.create"))
				{
					String arenaName = line1;
					Arena foundArena = null;

					for (Arena arena : UltimateGames.arenas)
					{
						if (arena.getName().equalsIgnoreCase(arenaName))
						{
							foundArena = arena;
							break;
						}
					}

					if (foundArena != null)
					{
						signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Texts.of(TextColors.DARK_BLUE, "[UltimateGames]")));
						signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(1, Texts.of(TextColors.GREEN, "Arena: ", TextColors.GRAY, arenaName)));
						UltimateGameSign gameSign = new UltimateGameSign(signLocation, foundArena);
						UltimateGames.gameSigns.add(gameSign);
						player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Successfully created UltimateGames sign!"));

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
									data = data.set(data.getValue(Keys.SIGN_LINES).get().set(2, Texts.of(TextColors.BLUE, "Team A: ", TextColors.GRAY, teamASize)));
									data = data.set(data.getValue(Keys.SIGN_LINES).get().set(3, Texts.of(TextColors.RED, "Teeam B: ", TextColors.GRAY, teamBSize)));
									tileEntity.offer(data);
								}

								if (!Ember.getMinigame(gameSign.getArena()).isPresent() && gameSign.getArena() instanceof DeathmatchArena && (gameSign.getTeamA().size() >= 1 && gameSign.getTeamB().size() >= 1))
								{
									DeathmatchArena dmArena = (DeathmatchArena) gameSign.getArena();
									List<Player> teamA = Lists.newArrayList();
									List<Player> teamB = Lists.newArrayList();
									
									for(Player p : UltimateGames.game.getServer().getOnlinePlayers())
									{
										if(gameSign.getTeamA().contains(p.getUniqueId()))
										{
											teamA.add(p);
										}
									}
									
									for(Player p : UltimateGames.game.getServer().getOnlinePlayers())
									{
										if(gameSign.getTeamB().contains(p.getUniqueId()))
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
								}
							}
						}).interval(1, TimeUnit.MILLISECONDS).name("UltimateGames - Update UltimateGamesSign").submit(UltimateGames.game.getPluginManager().getPlugin("UltimateGames").get().getInstance().get());
					}
				}
				else
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Texts.of(TextColors.DARK_RED, "[UltimateGames]")));
					player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to create UltimateGames signs."));
				}
			}
		}
	}
}
