package io.github.hsyyid.ultimategames.commands.arena;

import com.dracade.ember.core.SpawnPoint;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.DeathmatchArena;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class CreateArenaExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String type = ctx.<String> getOne("type").get();
		String name = ctx.<String> getOne("name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			SpawnPoint spawnpoint = new SpawnPoint(player.getLocation().getPosition(), player.getRotation(), player.getWorld());

			if (type.equalsIgnoreCase("Deathmatch") || type.equalsIgnoreCase("dm"))
			{
				DeathmatchArena arena = new DeathmatchArena(name, spawnpoint);
				UltimateGames.arenas.add(arena);
				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Created deathmatch arena. Spawn set to your current location!"));
				player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "Do not forget to set spawns for both team a and b!"));
			}
		}
		else
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You may only use this command as an in-game player."));
		}

		return CommandResult.success();
	}
}
