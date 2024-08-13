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
import me.whereareiam.socialismus.module.chirper.api.model.announcer.Announcer;
import me.whereareiam.socialismus.module.chirper.configuration.dynamic.AnnouncersConfig;
import me.whereareiam.socialismus.module.chirper.configuration.template.AnnouncerTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Singleton
public class AnnouncersProvider implements Provider<List<Announcer>>, Reloadable {
    private final Path announcersPath;
    private final LoggingHelper loggingHelper;
    private final ConfigurationLoader configLoader;
    private final ConfigurationType configurationType;

    private List<Announcer> announcers;

    @Inject
    public AnnouncersProvider(@Named("announcersPath") Path announcersPath, LoggingHelper loggingHelper, ConfigurationLoader configLoader,
                              ConfigurationManager configManager, AnnouncerTemplate template, Registry<Reloadable> registry) {
        this.announcersPath = announcersPath;
        this.loggingHelper = loggingHelper;
        this.configLoader = configLoader;
        this.configurationType = configManager.getConfigurationType();

        configManager.addTemplate(AnnouncersConfig.class, template);

        registry.register(this);
    }

    @Override
    public List<Announcer> get() {
        if (announcers != null) return announcers;

        loadAnnouncers();

        return announcers;
    }

    @Override
    public void reload() {
        loadAnnouncers();
    }

    private void loadAnnouncers() {
        announcers = new ArrayList<>();
        try (Stream<Path> paths = Files.list(announcersPath)) {
            paths.filter(path -> path.getFileName().toString().endsWith(configurationType.getExtension())).forEach(path -> {
                String fileName = path.getFileName().toString().replace(configurationType.getExtension(), "");

                if (Files.isDirectory(path) || fileName.isEmpty()) return;

                announcers.addAll(addAnnouncersFromConfig(path.getParent().resolve(fileName)));
            });
        } catch (IOException e) {
            loggingHelper.severe("Failed to load announcers configurations", e);
            announcers = Collections.emptyList();
            return;
        }

        if (announcers.isEmpty())
            announcers.addAll(addAnnouncersFromConfig(announcersPath.resolve("default")));

        announcers.removeIf(announcer -> announcers.stream().anyMatch(c -> c != announcer));
    }

    private List<Announcer> addAnnouncersFromConfig(Path path) {
        AnnouncersConfig announcersConfig = configLoader.load(path, AnnouncersConfig.class);
        return announcersConfig.getAnnouncers().stream()
                .filter(Announcer::isEnabled)
                .toList();
    }
}