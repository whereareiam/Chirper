package me.whereareiam.socialismus.module.chirper.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.announcer.Announcer;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import me.whereareiam.socialismus.module.chirper.configuration.provider.AnnouncementsProvider;
import me.whereareiam.socialismus.module.chirper.configuration.provider.AnnouncersProvider;
import me.whereareiam.socialismus.module.chirper.configuration.provider.ChirperCommandsProvider;
import me.whereareiam.socialismus.module.chirper.configuration.provider.ChirperMessagesProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ConfigBinder extends AbstractModule {
    private final Path workingPath;
    private final Path announcementsPath;
    private final Path announcersPath;

    public ConfigBinder(Path workingDirectory) {
        this.workingPath = workingDirectory;
        this.announcementsPath = workingDirectory.resolve("announcements");
        this.announcersPath = workingDirectory.resolve("announcers");
    }

    @Override
    protected void configure() {
        bind(Path.class).annotatedWith(Names.named("workingPath")).toInstance(workingPath);
        bind(Path.class).annotatedWith(Names.named("announcementsPath")).toInstance(announcementsPath);
        bind(Path.class).annotatedWith(Names.named("announcersPath")).toInstance(announcersPath);
        createDirectories();

        bind(AnnouncementsProvider.class).asEagerSingleton();
        bind(new TypeLiteral<List<Announcement>>() {}).toProvider(AnnouncementsProvider.class);

        bind(AnnouncersProvider.class).asEagerSingleton();
        bind(new TypeLiteral<List<Announcer>>() {}).toProvider(AnnouncersProvider.class);

        bind(ChirperCommandsProvider.class);
        bind(ChirperCommands.class).toProvider(ChirperCommandsProvider.class);

        bind(ChirperMessagesProvider.class);
        bind(ChirperMessages.class).toProvider(ChirperMessagesProvider.class);
    }

    private void createDirectories() {
        try {
            Files.createDirectories(workingPath);
            Files.createDirectories(announcementsPath);
            Files.createDirectories(announcersPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }
}
