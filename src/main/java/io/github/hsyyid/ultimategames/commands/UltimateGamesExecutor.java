package io.github.hsyyid.ultimategames.commands;

import io.github.hsyyid.ultimategames.UltimateGames;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class UltimateGamesExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GRAY, "Version: ", TextColors.GOLD, UltimateGames.game.getPluginManager().getPlugin("io.github.hsyyid.ultimategames").get().getVersion()));
		return CommandResult.success();
	}
}
