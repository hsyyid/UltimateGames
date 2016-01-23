package io.github.hsyyid.ultimategames.listeners;

import com.google.common.collect.Sets;
import io.github.hsyyid.ultimategames.UltimateGames;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Set;

public class PlayerDisconnectListener
{
	@Listener
	public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event)
	{
		Set<UltimateGameSign> foundGameSigns = Sets.newHashSet();
		
		for(UltimateGameSign gameSign: UltimateGames.gameSigns)
		{
			if(gameSign.teamA.contains(event.getTargetEntity().getUniqueId().toString()) || gameSign.teamB.contains(event.getTargetEntity().getUniqueId().toString()))
			{
				foundGameSigns.add(gameSign);
			}
		}
		
		for(UltimateGameSign gameSign : foundGameSigns)
		{
			for (String uuid : gameSign.getTeamA())
			{
				if (uuid.equals(event.getTargetEntity().getUniqueId().toString()))
				{
					gameSign.teamA.remove(event.getTargetEntity().getUniqueId().toString());
				}
			}

			for (String uuid : gameSign.getTeamB())
			{
				if (uuid.equals(event.getTargetEntity().getUniqueId().toString()))
				{
					gameSign.teamB.remove(event.getTargetEntity().getUniqueId().toString());
				}
			}
		}
	}
}
