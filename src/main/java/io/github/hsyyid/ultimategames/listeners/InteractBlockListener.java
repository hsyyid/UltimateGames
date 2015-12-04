package io.github.hsyyid.ultimategames.listeners;

import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class InteractBlockListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			
			if(event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN) ||
				event.getTargetBlock().getState().getType().equals(BlockTypes.STANDING_SIGN))
			{
				Location<World> blockLocation = event.getTargetBlock().getLocation().get();
				UltimateGameSign foundSign = null;
				
				for(UltimateGameSign gameSign : UltimateGames.gameSigns)
				{
					Location<World> gameSignLocation = gameSign.getLocation();
					
					if(gameSignLocation.getX() == blockLocation.getX() &&
						gameSignLocation.getY() == blockLocation.getY() && 
						gameSignLocation.getZ() == blockLocation.getZ() && 
						gameSignLocation.getExtent().getUniqueId().equals(blockLocation.getExtent().getUniqueId()))
					{
						foundSign = gameSign;
						break;
					}
				}
				
				if(foundSign != null)
				{
					if(player.hasPermission("ultimategames.signs.use"))
					{
						if(foundSign.getTeamA().size() == 0)
						{
							foundSign.teamA.add(player.getUniqueId());
							player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Joined UltimateGames queue to play on arena " + foundSign.getArena().getName()));
						}
						else if (foundSign.getTeamA().size() < foundSign.getTeamB().size())
						{
							foundSign.teamA.add(player.getUniqueId());
							player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Joined UltimateGames queue to play on arena " + foundSign.getArena().getName()));
						}
						else
						{
							foundSign.teamB.add(player.getUniqueId());
							player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.GREEN, "Joined UltimateGames queue to play on arena " + foundSign.getArena().getName()));
						}
						
					}
					else
					{
						player.sendMessage(Texts.of(TextColors.BLUE, "[UltimateGames]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to use UltimateGames signs."));
					}
				}
			}
		}
	}
}
