package me.whereareiam.socialismus.module.chirper.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.config.ConfigurationTypeResolver;
import me.whereareiam.socialismus.logging.Logger;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.configuration.dynamic.AnnouncementsConfig;
import me.whereareiam.socialismus.module.chirper.configuration.template.AnnouncementTemplate;
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
public class AnnouncementsProvider implements Provider<List<Announcement>>, Reloadable {
	private final Path announcementsPath;
	private final ConfigurationType configurationType;

	private List<Announcement> announcements;

	@Inject
	public AnnouncementsProvider(
			@Named("announcementsPath") Path announcementsPath,
			ConfigurationTypeResolver typeResolver,
			Registry<Reloadable> registry
	) {
		this.announcementsPath = announcementsPath;
		this.configurationType = typeResolver.getConfigurationType();

		Config.registerTemplate(AnnouncementTemplate.class);
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
			paths.filter(Files::isRegularFile)
					.filter(path -> path.getFileName().toString().endsWith(configurationType.getExtension()))
					.forEach(path -> {
						String fileName = path.getFileName().toString();
						// Remove the configured extension
						fileName = fileName.substring(0, fileName.length() - configurationType.getExtension().length());

						if (fileName.isEmpty()) return;

						announcements.addAll(addAnnouncementsFromConfig(path.getParent().resolve(fileName)));
					});
		} catch (IOException e) {
			Logger.severe("Failed to load announcement configurations: " + e.getMessage());
			announcements = Collections.emptyList();
			return;
		}

		if (announcements.isEmpty())
			announcements.addAll(addAnnouncementsFromConfig(announcementsPath.resolve("default")));

		// Remove duplicates by ID
		announcements.removeIf(announcement -> announcements.stream()
				.anyMatch(c -> c != announcement && c.getId().equals(announcement.getId())));
	}

	private List<Announcement> addAnnouncementsFromConfig(Path path) {
		AnnouncementsConfig config = Config.update(path, AnnouncementsConfig.class);
		return config.getAnnouncements().stream()
				.filter(Announcement::isEnabled)
				.toList();
	}
}