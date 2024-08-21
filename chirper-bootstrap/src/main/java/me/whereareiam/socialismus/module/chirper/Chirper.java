package me.whereareiam.socialismus.module.chirper;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
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
import me.whereareiam.socialismus.api.output.module.SocialisticModule;
import me.whereareiam.socialismus.api.type.requirement.RequirementType;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import me.whereareiam.socialismus.module.chirper.command.CommandRegistrar;
import me.whereareiam.socialismus.module.chirper.common.AnnouncerController;
import me.whereareiam.socialismus.module.chirper.common.CommonConfiguration;
import me.whereareiam.socialismus.module.chirper.configuration.ConfigBinder;

import java.util.Map;

public class Chirper extends SocialisticModule {
    private final Injector parentInjector;
    private final Registry<Reloadable> reloadableRegistry;
    private final Registry<Map<String, CommandEntity>> commandRegistry;
    private final ExtendedRegistry<RequirementType, RequirementValidation> requirementRegistry;
    private Injector injector;

    @Inject
    public Chirper(Injector parentInjector, Registry<Reloadable> reloadableRegistry, Registry<Map<String, CommandEntity>> commandRegistry,
                   ExtendedRegistry<RequirementType, RequirementValidation> requirementRegistry) {
        this.parentInjector = parentInjector;
        this.reloadableRegistry = reloadableRegistry;
        this.commandRegistry = commandRegistry;
        this.requirementRegistry = requirementRegistry;
    }

    @Override
    public void onLoad() {
        injector = Guice.createInjector(
                new ChirperInjectorConfiguration(
                        parentInjector.getInstance(Scheduler.class),
                        parentInjector.getInstance(PlatformInteractor.class),
                        parentInjector.getInstance(SerializationService.class),
                        reloadableRegistry,
                        commandRegistry,
                        requirementRegistry,
                        parentInjector.getInstance(LoggingHelper.class),
                        parentInjector.getInstance(ConfigurationManager.class),
                        parentInjector.getInstance(ConfigurationLoader.class),
                        parentInjector.getInstance(CommandService.class),
                        parentInjector.getInstance(PlayerContainerService.class)
                ),
                new ConfigBinder(workingPath),
                new CommonConfiguration()
        );

        injector.getInstance(ChirperMessages.class);
        injector.getInstance(ChirperCommands.class);
    }

    @Override
    public void onEnable() {
        injector.getInstance(CommandRegistrar.class).registerCommands();
        injector.getInstance(AnnouncerController.class).init();
    }

    @Override
    public void onDisable() {
        injector.getInstance(Scheduler.class).cancelByModule("chirper");
    }

    @Override
    public void onUnload() {

    }
}
