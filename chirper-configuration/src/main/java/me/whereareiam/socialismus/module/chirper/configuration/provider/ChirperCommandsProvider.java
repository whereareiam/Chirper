package me.whereareiam.socialismus.module.chirper.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.configuration.template.ChirperCommandsTemplate;

import java.nio.file.Path;

@Singleton
public class ChirperCommandsProvider implements Provider<ChirperCommands>, Reloadable {
    private final Path workingPath;
    private final ConfigurationLoader configLoader;

    private ChirperCommands commands;

    @Inject
    public ChirperCommandsProvider(@Named("workingPath") Path workingPath, ConfigurationLoader configLoader, ConfigurationManager configManager,
                                   ChirperCommandsTemplate template, Registry<Reloadable> reloadableRegistry) {
        this.workingPath = workingPath;
        this.configLoader = configLoader;

        configManager.addTemplate(ChirperCommands.class, template);

        reloadableRegistry.register(this);
        get();
    }

    @Override
    public ChirperCommands get() {
        if (commands != null) return commands;

        load();

        return commands;
    }

    @Override
    public void reload() {
        load();
    }

    private void load() {
        commands = configLoader.load(workingPath.resolve("commands"), ChirperCommands.class);
    }
}
