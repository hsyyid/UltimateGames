package io.github.hsyyid.ultimategames;

import com.dracade.ember.Ember;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.github.hsyyid.ultimategames.arenas.UltimateGamesArena;
import io.github.hsyyid.ultimategames.commands.UltimateGamesExecutor;
import io.github.hsyyid.ultimategames.commands.arena.CreateArenaExecutor;
import io.github.hsyyid.ultimategames.commands.arena.DeleteArenaExecutor;
import io.github.hsyyid.ultimategames.commands.arena.SetTeamLoadoutExecutor;
import io.github.hsyyid.ultimategames.commands.arena.SetTeamSpawnExecutor;
import io.github.hsyyid.ultimategames.listeners.BreakBlockListener;
import io.github.hsyyid.ultimategames.listeners.InteractBlockListener;
import io.github.hsyyid.ultimategames.listeners.PlayerDisconnectListener;
import io.github.hsyyid.ultimategames.listeners.SignChangeListener;
import io.github.hsyyid.ultimategames.utils.ConfigManager;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import io.github.hsyyid.ultimategames.utils.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Plugin(id = "UltimateGames", name = "UltimateGames", version = "0.2", dependencies = "required-after:EMBER")
public class UltimateGames
{
	public static Game game;
	public static ConfigurationNode config;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static List<UltimateGamesArena> arenas = Lists.newArrayList();
	public static List<UltimateGameSign> gameSigns = Lists.newArrayList();

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File dConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> confManager;

	@Listener
	public void onServerInit(GameInitializationEvent event)
	{
		getLogger().info("UltimateGames loading...");

		game = Sponge.getGame();

		try
		{
			if (!dConfig.exists())
			{
				dConfig.createNewFile();
				config = confManager.load();
				confManager.save(config);
			}
			configurationManager = confManager;
			config = confManager.load();

		}
		catch (IOException exception)
		{
			getLogger().error("The default configuration could not be loaded or created!");
		}

		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("create"), CommandSpec.builder()
			.description(Text.of("Create Arena Command"))
			.permission("ultimategames.command.create")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("type"))), GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))), GenericArguments.onlyOne(GenericArguments.integer(Text.of("team size")))))
			.executor(new CreateArenaExecutor())
			.build());

		subcommands.put(Arrays.asList("delete", "remove"), CommandSpec.builder()
			.description(Text.of("Remove Arena Command"))
			.permission("ultimategames.command.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
			.executor(new DeleteArenaExecutor())
			.build());

		subcommands.put(Arrays.asList("setteamspawn"), CommandSpec.builder()
			.description(Text.of("Set Team Spawn Command"))
			.permission("ultimategames.command.teamspawn.set")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("arena"))), GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))))
			.executor(new SetTeamSpawnExecutor())
			.build());

		subcommands.put(Arrays.asList("setteamloadout"), CommandSpec.builder()
			.description(Text.of("Set Team Loadout Command"))
			.permission("ultimategames.command.teamloadout.set")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("arena"))), GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))), GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("loadout")))))
			.executor(new SetTeamLoadoutExecutor())
			.build());

		CommandSpec ultimateGamesCommandSpec = CommandSpec.builder()
			.description(Text.of("Ultimate Games Command"))
			.permission("ultimategames.use")
			.executor(new UltimateGamesExecutor())
			.children(subcommands)
			.build();

		game.getCommandManager().register(this, ultimateGamesCommandSpec, "ultimategames", "ug");

		game.getEventManager().registerListeners(this, new InteractBlockListener());
		game.getEventManager().registerListeners(this, new SignChangeListener());
		game.getEventManager().registerListeners(this, new BreakBlockListener());
		game.getEventManager().registerListeners(this, new PlayerDisconnectListener());

		getLogger().info("-----------------------------");
		getLogger().info("UltimateGames was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("UltimateGames loaded!");
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event)
	{
		String json = ConfigManager.readJSON();

		if (json != null && json.length() > 0)
		{
			try
			{
				UltimateGames.arenas = Arrays.asList(Ember.serializer().gson().fromJson(json, UltimateGamesArena[].class));
			}
			catch (Exception e)
			{
				getLogger().error("An error occured while reading the arenas!");
				e.printStackTrace();
			}
		}

		json = ConfigManager.readSignsJSON();

		if (json != null && json.length() > 0)
		{
			try
			{
				UltimateGames.gameSigns = Arrays.asList(Ember.serializer().gson().fromJson(json, UltimateGameSign[].class));

				for (UltimateGameSign gameSign : UltimateGames.gameSigns)
				{
					Utils.startSignService(gameSign, gameSign.getLocation(), gameSign.getArena());
				}
			}
			catch (Exception e)
			{
				getLogger().error("An error occured while reading the UltimateGame Signs!");
				e.printStackTrace();
			}
		}
	}

	@Listener
	public void onServerStopping(GameStoppedServerEvent event)
	{
		try
		{
			String json = Ember.serializer().gson().toJson(UltimateGames.arenas);
			ConfigManager.writeJSON(json);
		}
		catch (Exception e)
		{
			getLogger().error("There was an issue while saving the arenas!");
			e.printStackTrace();
		}

		try
		{
			String json = Ember.serializer().gson().toJson(UltimateGames.gameSigns);
			ConfigManager.writeSignJSON(json);
		}
		catch (Exception e)
		{
			getLogger().error("There was an issue while saving the game signs!");
			e.printStackTrace();
		}

		getLogger().info("UltimateGames disabled.");
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
