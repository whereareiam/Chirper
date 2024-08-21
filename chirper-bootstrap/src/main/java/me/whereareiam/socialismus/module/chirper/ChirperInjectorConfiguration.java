package me.whereareiam.socialismus.module.chirper;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.chat.RequirementValidation;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.input.registry.ExtendedRegistry;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.input.serializer.SerializationService;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.LoggingHelper;
import me.whereareiam.socialismus.api.output.PlatformInteractor;
import me.whereareiam.socialismus.api.output.Scheduler;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.api.type.requirement.RequirementType;

import java.util.Map;

public class ChirperInjectorConfiguration extends AbstractModule {
    private final Scheduler scheduler;
    private final PlatformInteractor platformInteractor;
    private final SerializationService serializationService;

    private final Registry<Reloadable> reloadableRegistry;
    private final Registry<Map<String, CommandEntity>> commandRegistry;
    private final ExtendedRegistry<RequirementType, RequirementValidation> requirementRegistry;

    private final LoggingHelper loggingHelper;
    private final ConfigurationManager configurationManager;
    private final ConfigurationLoader configurationLoader;

    private final CommandService commandService;
    private final PlayerContainerService playerContainerService;

    public ChirperInjectorConfiguration(Scheduler scheduler, PlatformInteractor platformInteractor, SerializationService serializationService, Registry<Reloadable> reloadableRegistry, Registry<Map<String, CommandEntity>> commandRegistry,
                                        ExtendedRegistry<RequirementType, RequirementValidation> requirementRegistry, LoggingHelper loggingHelper, ConfigurationManager configurationManager, ConfigurationLoader configurationLoader, CommandService commandService,
                                        PlayerContainerService playerContainerService) {
        this.scheduler = scheduler;
        this.platformInteractor = platformInteractor;
        this.serializationService = serializationService;

        this.reloadableRegistry = reloadableRegistry;
        this.commandRegistry = commandRegistry;
        this.requirementRegistry = requirementRegistry;

        this.loggingHelper = loggingHelper;
        this.configurationManager = configurationManager;
        this.configurationLoader = configurationLoader;

        this.commandService = commandService;
        this.playerContainerService = playerContainerService;
    }

    @Override
    protected void configure() {
        bind(Scheduler.class).toInstance(scheduler);
        bind(PlatformInteractor.class).toInstance(platformInteractor);
        bind(SerializationService.class).toInstance(serializationService);

        bind(new TypeLiteral<Registry<Reloadable>>() {}).toInstance(reloadableRegistry);
        bind(new TypeLiteral<Registry<Map<String, CommandEntity>>>() {}).toInstance(commandRegistry);
        bind(new TypeLiteral<ExtendedRegistry<RequirementType, RequirementValidation>>() {}).toInstance(requirementRegistry);

        bind(LoggingHelper.class).toInstance(loggingHelper);
        bind(ConfigurationManager.class).toInstance(configurationManager);
        bind(ConfigurationLoader.class).toInstance(configurationLoader);

        bind(CommandService.class).toInstance(commandService);
        bind(PlayerContainerService.class).toInstance(playerContainerService);
    }
}
