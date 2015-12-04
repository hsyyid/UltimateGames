package io.github.hsyyid.ultimategames.commands.minigame;

import com.dracade.ember.core.Arena;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.DeathmatchArena;
import io.github.hsyyid.ultimategames.minigames.DeathmatchMinigame;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Arrays;

public class StartMinigameExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String arena = ctx.<String> getOne("arena").get();
		Player player1 = ctx.<Player> getOne("p1").get();
		Player player2 = ctx.<Player> getOne("p2").get();

		DeathmatchArena dmArena = null;

		for (Arena a : UltimateGames.arenas)
		{
			if (a.getName().equalsIgnoreCase(arena) && a instanceof DeathmatchArena)
			{
				dmArena = (DeathmatchArena) a;
			}
		}

		if (dmArena != null)
		{
			try
			{
				new DeathmatchMinigame(dmArena, Arrays.asList(player1), Arrays.asList(player2));
				src.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Minigame started!"));
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				src.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "Minigame failed to start!"));
			}
		}
		else
		{
			src.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "Arena does not exist!"));
		}

		return CommandResult.success();
	}
}
