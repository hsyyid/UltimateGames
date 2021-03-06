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

public class CreateArenaExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String type = ctx.<String> getOne("type").get();
		String name = ctx.<String> getOne("name").get();
		int teamSize = ctx.<Integer> getOne("team size").get();
		int length = ctx.<Integer> getOne("length").orElse(5);

		if (src instanceof Player)
		{
			Player player = (Player) src;
			SpawnPoint spawnpoint = new SpawnPoint(player.getLocation().getPosition(), player.getRotation(), player.getWorld());

			if (Utils.getArena(name).isPresent())
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "An arena with this name already exists!"));
				return CommandResult.success();
			}

			if (type.equalsIgnoreCase("Deathmatch") || type.equalsIgnoreCase("dm"))
			{
				UltimateGamesArena arena = new UltimateGamesArena(name, spawnpoint, teamSize, length);
				UltimateGames.arenas.add(arena);
				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Created deathmatch arena. Spawn set to your current location!"));
				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "Do not forget to set spawns for both team a and b!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You may only use this command as an in-game player."));
		}

		return CommandResult.success();
	}
}
