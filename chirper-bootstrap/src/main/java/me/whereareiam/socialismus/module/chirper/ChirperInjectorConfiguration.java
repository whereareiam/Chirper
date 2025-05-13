package me.whereareiam.socialismus.module.chirper;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.input.requirement.RequirementEvaluatorService;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.PlatformInteractor;
import me.whereareiam.socialismus.api.output.Scheduler;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;

import java.util.Map;

public class ChirperInjectorConfiguration extends AbstractModule {
	private final Scheduler scheduler;
	private final PlatformInteractor platformInteractor;

	private final Registry<Reloadable> reloadableRegistry;
	private final Registry<Map<String, CommandEntity>> commandRegistry;
	private final RequirementEvaluatorService requirementEvaluator;

	private final ConfigurationManager configurationManager;
	private final ConfigurationLoader configurationLoader;

	private final CommandService commandService;
	private final PlayerContainerService playerContainerService;

	public ChirperInjectorConfiguration(
			Scheduler scheduler,
			PlatformInteractor platformInteractor,
			Registry<Reloadable> reloadableRegistry,
			Registry<Map<String, CommandEntity>> commandRegistry,
			RequirementEvaluatorService requirementEvaluator,
			ConfigurationManager configurationManager,
			ConfigurationLoader configurationLoader,
			CommandService commandService,
			PlayerContainerService playerContainerService
	) {
		this.scheduler = scheduler;
		this.platformInteractor = platformInteractor;

		this.reloadableRegistry = reloadableRegistry;
		this.commandRegistry = commandRegistry;
		this.requirementEvaluator = requirementEvaluator;

		this.configurationManager = configurationManager;
		this.configurationLoader = configurationLoader;

		this.commandService = commandService;
		this.playerContainerService = playerContainerService;
	}

	@Override
	protected void configure() {
		bind(Scheduler.class).toInstance(scheduler);
		bind(PlatformInteractor.class).toInstance(platformInteractor);
		bind(RequirementEvaluatorService.class).toInstance(requirementEvaluator);

		bind(new TypeLiteral<Registry<Reloadable>>() {}).toInstance(reloadableRegistry);
		bind(new TypeLiteral<Registry<Map<String, CommandEntity>>>() {}).toInstance(commandRegistry);

		bind(ConfigurationManager.class).toInstance(configurationManager);
		bind(ConfigurationLoader.class).toInstance(configurationLoader);

		bind(CommandService.class).toInstance(commandService);
		bind(PlayerContainerService.class).toInstance(playerContainerService);
	}
}
