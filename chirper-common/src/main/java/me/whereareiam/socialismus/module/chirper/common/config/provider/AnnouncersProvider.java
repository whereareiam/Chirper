package me.whereareiam.socialismus.module.chirper.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.config.ConfigurationTypeResolver;
import me.whereareiam.socialismus.logging.Logger;
import me.whereareiam.socialismus.module.chirper.api.model.announcer.Announcer;
import me.whereareiam.socialismus.module.chirper.common.config.dynamic.AnnouncersConfig;
import me.whereareiam.socialismus.module.chirper.common.config.template.AnnouncerTemplate;
import me.whereareiam.socialismus.registry.base.Registry;
import me.whereareiam.socialismus.type.ConfigurationType;

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
	private final ConfigurationType configurationType;

	private List<Announcer> announcers;

	@Inject
	public AnnouncersProvider(
			@Named("announcersPath") Path announcersPath,
			ConfigurationTypeResolver typeResolver,
			Registry<Reloadable> registry
	) {
		this.announcersPath = announcersPath;
		this.configurationType = typeResolver.getConfigurationType();

		Config.registerTemplate(AnnouncerTemplate.class);
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
			paths.filter(Files::isRegularFile)
					.filter(path -> path.getFileName().toString().endsWith(configurationType.getExtension()))
					.forEach(path -> {
						String fileName = path.getFileName().toString();
						// Remove the configured extension
						fileName = fileName.substring(0, fileName.length() - configurationType.getExtension().length());

						if (fileName.isEmpty()) return;

						announcers.addAll(addAnnouncersFromConfig(path.getParent().resolve(fileName)));
					});
		} catch (IOException e) {
			Logger.severe("Failed to load announcers configurations: " + e.getMessage());
			announcers = Collections.emptyList();
			return;
		}

		if (announcers.isEmpty())
			announcers.addAll(addAnnouncersFromConfig(announcersPath.resolve("default")));

		// Remove duplicates
		announcers.removeIf(announcer -> announcers.stream().anyMatch(c -> c != announcer));
	}

	private List<Announcer> addAnnouncersFromConfig(Path path) {
		AnnouncersConfig config = Config.update(path, AnnouncersConfig.class);
		return config.getAnnouncers().stream()
				.filter(Announcer::isEnabled)
				.toList();
	}
}
