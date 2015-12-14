package io.github.hsyyid.ultimategames;

import com.dracade.ember.Ember;
import com.dracade.ember.core.Arena;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.github.hsyyid.ultimategames.commands.UltimateGamesExecutor;
import io.github.hsyyid.ultimategames.commands.arena.CreateArenaExecutor;
import io.github.hsyyid.ultimategames.commands.arena.SetTeamSpawnExecutor;
import io.github.hsyyid.ultimategames.listeners.BreakBlockListener;
import io.github.hsyyid.ultimategames.listeners.InteractBlockListener;
import io.github.hsyyid.ultimategames.listeners.SignChangeListener;
import io.github.hsyyid.ultimategames.utils.ConfigManager;
import io.github.hsyyid.ultimategames.utils.UltimateGameSign;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Texts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Plugin(id = "UltimateGames", name = "UltimateGames", version = "0.1", dependencies = "required-after:TotalEconomy;required-after:EMBER")
public class UltimateGames
{
	public static Game game;
	public static ConfigurationNode config;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static List<Arena> arenas = Lists.newArrayList();
	public static Set<UltimateGameSign> gameSigns = Sets.newHashSet();

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

		game = event.getGame();

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
			.description(Texts.of("Create Arena Command"))
			.permission("ultimategames.create")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.string(Texts.of("type"))),
				GenericArguments.onlyOne(GenericArguments.string(Texts.of("name")))))
			.executor(new CreateArenaExecutor())
			.build());

		subcommands.put(Arrays.asList("setteamspawn"), CommandSpec.builder()
			.description(Texts.of("Set Team Spawn Command"))
			.permission("ultimategames.teamspawn.set")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.string(Texts.of("arena"))),
				GenericArguments.onlyOne(GenericArguments.string(Texts.of("team")))))
			.executor(new SetTeamSpawnExecutor())
			.build());

		CommandSpec ultimateGamesCommandSpec = CommandSpec.builder()
			.description(Texts.of("Ultimate Games Command"))
			.permission("ultimategames.use")
			.executor(new UltimateGamesExecutor())
			.children(subcommands)
			.build();

		game.getCommandManager().register(this, ultimateGamesCommandSpec, "ultimategames", "ug");
		
		game.getEventManager().registerListeners(this, new InteractBlockListener());
		game.getEventManager().registerListeners(this, new SignChangeListener());
		game.getEventManager().registerListeners(this, new BreakBlockListener());
		
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
				UltimateGames.arenas = Arrays.asList(Ember.serializer().gson().fromJson(json, Arena[].class));
			}
			catch (Exception e)
			{
				getLogger().error("An error occured while reading the arenas!");
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
			e.printStackTrace();
			getLogger().error("There was an issue while saving the arenas!");
		}

		getLogger().info("UltimateGames disabled.");
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
