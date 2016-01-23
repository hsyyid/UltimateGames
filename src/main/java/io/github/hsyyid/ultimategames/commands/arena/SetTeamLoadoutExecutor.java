package io.github.hsyyid.ultimategames.commands.arena;

import com.dracade.ember.core.Arena;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.arenas.UltimateGamesArena;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetTeamLoadoutExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String arena = ctx.<String> getOne("arena").get();
		String team = ctx.<String> getOne("team").get();
		String loadout = ctx.<String> getOne("loadout").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			UltimateGamesArena foundArena = null;

			for (Arena a : UltimateGames.arenas)
			{
				if (a.getName().equalsIgnoreCase(arena) && a instanceof UltimateGamesArena)
				{
					foundArena = (UltimateGamesArena) a;
					break;
				}
			}

			if (foundArena != null)
			{
				UltimateGames.arenas.remove(foundArena);

				if (team.equalsIgnoreCase("a"))
				{
					foundArena.setTeamALoadout(loadout);
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Team A loadout set!"));
				}
				else if (team.equalsIgnoreCase("b"))
				{
					foundArena.setTeamBLoadout(loadout);
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Team B loadout set!"));
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
