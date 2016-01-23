package io.github.hsyyid.ultimategames.listeners;

import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import io.github.hsyyid.ultimategames.utils.Utils;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
			String line0 = signData.getValue(Keys.SIGN_LINES).get().get(0).toPlain();
			String line1 = signData.getValue(Keys.SIGN_LINES).get().get(1).toPlain();

			if (line0.equals("[UltimateGames]"))
			{
				if (player.hasPermission("ultimategames.signs.create"))
				{
					String arenaName = line1;

					if (Utils.getArena(arenaName).isPresent())
					{
						signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_BLUE, "[UltimateGames]")));
						signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(1, Text.of(TextColors.GREEN, "Arena: ", TextColors.GRAY, arenaName)));
						UltimateGameSign gameSign = new UltimateGameSign(signLocation, arenaName);
						UltimateGames.gameSigns.add(gameSign);
						player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Successfully created UltimateGames sign!"));
						Utils.startSignService(gameSign, signLocation, arenaName);
					}
				}
				else
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_RED, "[UltimateGames]")));
					player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to create UltimateGames signs."));
				}
			}
		}
	}
}
