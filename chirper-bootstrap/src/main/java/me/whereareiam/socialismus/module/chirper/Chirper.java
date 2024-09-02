package me.whereareiam.socialismus.module.chirper;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.input.requirement.RequirementEvaluatorService;
import me.whereareiam.socialismus.api.input.serializer.SerializationService;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.LoggingHelper;
import me.whereareiam.socialismus.api.output.PlatformInteractor;
import me.whereareiam.socialismus.api.output.Scheduler;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.api.output.module.SocialisticModule;
import me.whereareiam.socialismus.module.chirper.command.CommandRegistrar;
import me.whereareiam.socialismus.module.chirper.common.AnnouncerController;
import me.whereareiam.socialismus.module.chirper.common.CommonConfiguration;
import me.whereareiam.socialismus.module.chirper.configuration.ConfigBinder;

import java.util.Map;

public class Chirper extends SocialisticModule {
    private final Injector parentInjector;
    private final Registry<Reloadable> reloadableRegistry;
    private final Registry<Map<String, CommandEntity>> commandRegistry;
    private Injector injector;

    @Inject
    public Chirper(Injector parentInjector, Registry<Reloadable> reloadableRegistry, Registry<Map<String, CommandEntity>> commandRegistry) {
        this.parentInjector = parentInjector;
        this.reloadableRegistry = reloadableRegistry;
        this.commandRegistry = commandRegistry;
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
                        parentInjector.getInstance(RequirementEvaluatorService.class),
                        parentInjector.getInstance(LoggingHelper.class),
                        parentInjector.getInstance(ConfigurationManager.class),
                        parentInjector.getInstance(ConfigurationLoader.class),
                        parentInjector.getInstance(CommandService.class),
                        parentInjector.getInstance(PlayerContainerService.class)
                ),
                new ConfigBinder(workingPath),
                new CommonConfiguration()
        );
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
