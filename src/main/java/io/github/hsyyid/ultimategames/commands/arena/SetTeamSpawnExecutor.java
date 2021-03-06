package io.github.hsyyid.ultimategames.commands.arena;

import com.dracade.ember.core.SpawnPoint;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.UltimateGamesArena;
import io.github.hsyyid.ultimategames.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetTeamSpawnExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String arena = ctx.<String> getOne("arena").get();
		String team = ctx.<String> getOne("team").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (Utils.getArena(arena).isPresent())
			{
				UltimateGamesArena foundArena = Utils.getArena(arena).get();
				UltimateGames.arenas.remove(foundArena);
				SpawnPoint spawnpoint = new SpawnPoint(player.getLocation().getPosition(), player.getRotation(), player.getWorld());

				if (team.equalsIgnoreCase("a"))
				{
					foundArena.setTeamASpawn(spawnpoint);
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Spawn for team A set to your current location!"));
				}
				else if (team.equalsIgnoreCase("b"))
				{
					foundArena.setTeamBSpawn(spawnpoint);
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Spawn for team B set to your current location!"));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You may only specify team A or team B!"));
				}

				UltimateGames.arenas.add(foundArena);
			}
			else
			{
				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Arena not found."));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You may only use this command as an in-game player."));
		}

		return CommandResult.success();
	}
}
