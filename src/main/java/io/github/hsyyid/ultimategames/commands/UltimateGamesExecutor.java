package io.github.hsyyid.ultimategames.commands;

import io.github.hsyyid.ultimategames.UltimateGames;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class UltimateGamesExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GRAY, "Version: ", TextColors.GOLD, UltimateGames.game.getPluginManager().getPlugin("UltimateGames").get().getVersion()));
		return CommandResult.success();
	}
}
