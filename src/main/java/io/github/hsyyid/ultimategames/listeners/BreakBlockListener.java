package io.github.hsyyid.ultimategames.listeners;

import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BreakBlockListener
{
	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event)
	{
		for (Transaction<BlockSnapshot> transaction : event.getTransactions())
		{
			if (transaction.getOriginal().getState().getType().equals(BlockTypes.WALL_SIGN) || transaction.getOriginal().getState().getType().equals(BlockTypes.STANDING_SIGN))
			{
				UltimateGameSign foundGameSign = null;
				Location<World> location = transaction.getOriginal().getLocation().get();

				for (UltimateGameSign gameSign : UltimateGames.gameSigns)
				{
					if (gameSign.getLocation().getExtent().getUniqueId().equals(event.getTargetWorld().getUniqueId()) && gameSign.getLocation().getBlockX() == location.getBlockX() && gameSign.getLocation().getBlockY() == location.getBlockY() && gameSign.getLocation().getBlockZ() == location.getBlockZ())
					{
						foundGameSign = gameSign;
						break;
					}
				}

				if (foundGameSign != null)
				{
					if (event.getCause().first(Player.class).isPresent())
					{
						Player player = (Player) event.getCause().first(Player.class).get();

						if (player.hasPermission("ultimategames.signs.destroy"))
						{
							UltimateGames.gameSigns.remove(foundGameSign);
							player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "GameSign removed!"));
						}
						else
						{
							player.sendMessage(Text.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.RED, "You do not have permission to remove Game-Signs!"));
							event.setCancelled(true);
						}
					}
					else
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
