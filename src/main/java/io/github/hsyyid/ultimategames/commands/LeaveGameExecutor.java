package io.github.hsyyid.ultimategames.commands;

import com.dracade.ember.Ember;
import com.dracade.ember.core.Minigame;
import io.github.hsyyid.ultimategames.minigames.DeathmatchMinigame;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class LeaveGameExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			DeathmatchMinigame foundDeathmatch = null;

			for (Minigame minigame : Ember.getMinigames())
			{
				if (minigame instanceof DeathmatchMinigame)
				{
					DeathmatchMinigame deathmatch = (DeathmatchMinigame) minigame;

					if (deathmatch.players().contains(player))
					{
						foundDeathmatch = deathmatch;
						break;
					}
				}
			}

			if (foundDeathmatch != null)
			{
				if (foundDeathmatch.teamA.contains(player))
					foundDeathmatch.teamA.remove(player);
				else
					foundDeathmatch.teamB.remove(player);

				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Left mini-game!"));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "You are not in a mini-game!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You may only use this command as an in-game player."));
		}

		return CommandResult.success();
	}
}
