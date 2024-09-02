package me.whereareiam.socialismus.module.chirper.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import me.whereareiam.socialismus.module.chirper.configuration.template.ChirperMessagesTemplate;

import java.nio.file.Path;

@Singleton
public class ChirperMessagesProvider implements Provider<ChirperMessages>, Reloadable {
    private final Path workingPath;
    private final ConfigurationLoader configLoader;
    private ChirperMessages messages;

    @Inject
    public ChirperMessagesProvider(@Named("workingPath") Path workingPath, ConfigurationLoader configLoader, ConfigurationManager configManager,
                                   ChirperMessagesTemplate template, Registry<Reloadable> registry) {
        this.workingPath = workingPath;
        this.configLoader = configLoader;

        configManager.addTemplate(ChirperMessages.class, template);
        
        registry.register(this);
        get();
    }

    @Override
    public ChirperMessages get() {
        if (messages != null) return messages;

        load();

        return messages;
    }

    @Override
    public void reload() {
        load();
    }

    private void load() {
        messages = configLoader.load(workingPath.resolve("messages"), ChirperMessages.class);
    }
}