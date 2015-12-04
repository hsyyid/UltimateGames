package io.github.hsyyid.ultimategames.listeners;

import com.dracade.ember.core.Arena;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.service.scheduler.Task;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
			Location<World> signLocation = sign.getLocation();
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
						UltimateGames.gameSigns.add(new UltimateGameSign(signLocation, foundArena));
						player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Successfully created UltimateGames sign!"));
					
						SchedulerService scheduler = UltimateGames.game.getScheduler();
						Task.Builder taskBuilder = scheduler.createTaskBuilder();

						taskBuilder.execute(() -> {
							;
						}).interval(1, TimeUnit.SECONDS).name("UltimateGames - Update UltimateGamesSign").submit(UltimateGames.game.getPluginManager().getPlugin("UltimateGames").get().getInstance());
					}
					else
					{
						signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Texts.of(TextColors.DARK_RED, "[UltimateGames]")));
						player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Failed to create UltimateGames sign: arena not found."));
					}
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to create UltimateGames signs."));
				}
			}
		}
	}
}
