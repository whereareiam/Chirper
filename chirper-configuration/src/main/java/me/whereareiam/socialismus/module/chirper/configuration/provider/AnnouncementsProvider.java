package me.whereareiam.socialismus.module.chirper.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.LoggingHelper;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.api.type.ConfigurationType;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.AnnouncementContent;
import me.whereareiam.socialismus.module.chirper.configuration.deserializer.AnnouncementDeserializer;
import me.whereareiam.socialismus.module.chirper.configuration.dynamic.AnnouncementsConfig;
import me.whereareiam.socialismus.module.chirper.configuration.template.AnnouncementTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Singleton
public class AnnouncementsProvider implements Provider<List<Announcement>>, Reloadable {
    private final Path announcementsPath;
    private final LoggingHelper loggingHelper;
    private final ConfigurationLoader configLoader;
    private final ConfigurationType configurationType;

    private List<Announcement> announcements;

    @Inject
    public AnnouncementsProvider(@Named("announcementsPath") Path announcementsPath, LoggingHelper loggingHelper, ConfigurationLoader configLoader,
                                 ConfigurationManager configManager, AnnouncementTemplate template, AnnouncementDeserializer contentDeserializer,
                                 Registry<Reloadable> registry) {
        this.announcementsPath = announcementsPath;
        this.loggingHelper = loggingHelper;
        this.configLoader = configLoader;
        this.configurationType = configManager.getConfigurationType();

        configManager.addTemplate(AnnouncementsConfig.class, template);
        configManager.addDeserializer(AnnouncementContent.class, contentDeserializer);

        registry.register(this);
    }

    @Override
    public List<Announcement> get() {
        if (announcements != null) return announcements;

        loadAnnouncements();

        return announcements;
    }

    @Override
    public void reload() {
        loadAnnouncements();
    }

    private void loadAnnouncements() {
        announcements = new ArrayList<>();
        try (Stream<Path> paths = Files.list(announcementsPath)) {
            paths.filter(path -> path.getFileName().toString().endsWith(configurationType.getExtension())).forEach(path -> {
                String fileName = path.getFileName().toString().replace(configurationType.getExtension(), "");

                if (Files.isDirectory(path) || fileName.isEmpty()) return;

                announcements.addAll(addAnnouncementsFromConfig(path.getParent().resolve(fileName)));
            });
        } catch (IOException e) {
            loggingHelper.severe("Failed to load announcement configurations", e);
            announcements = Collections.emptyList();
            return;
        }

        if (announcements.isEmpty())
            announcements.addAll(addAnnouncementsFromConfig(announcementsPath.resolve("default")));

        announcements.removeIf(announcement -> announcements.stream().anyMatch(c -> c != announcement && c.getId().equals(announcement.getId())));
    }

    private List<Announcement> addAnnouncementsFromConfig(Path path) {
        AnnouncementsConfig announcementsConfig = configLoader.load(path, AnnouncementsConfig.class);
        return announcementsConfig.getAnnouncements().stream()
                .filter(Announcement::isEnabled)
                .toList();
    }
}