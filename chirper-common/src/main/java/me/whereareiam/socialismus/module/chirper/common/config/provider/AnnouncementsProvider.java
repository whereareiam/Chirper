package me.whereareiam.socialismus.module.chirper.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.config.ConfigurationTypeResolver;
import me.whereareiam.socialismus.logging.Logger;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.common.config.ChirperConfigProvider;
import me.whereareiam.socialismus.module.chirper.common.config.dynamic.AnnouncementsConfig;
import me.whereareiam.socialismus.module.chirper.common.config.template.AnnouncementTemplate;
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
public class AnnouncementsProvider extends ChirperConfigProvider<List<Announcement>> {
	private final Path announcementsPath;
	private final ConfigurationType configurationType;

	@Inject
	public AnnouncementsProvider(
			@Named("announcementsPath") Path announcementsPath,
			@Named("workingPath") Path workingPath,
			ConfigurationTypeResolver typeResolver,
			Registry<Reloadable> registry
	) {
		super(workingPath, registry);
		this.announcementsPath = announcementsPath;
		this.configurationType = typeResolver.getConfigurationType();
	}

	@Override
	protected List<Announcement> load() {
		List<Announcement> announcements = new ArrayList<>();
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
			return Collections.emptyList();
		}

		if (announcements.isEmpty())
			announcements.addAll(addAnnouncementsFromConfig(announcementsPath.resolve("default")));

		// Remove duplicates by ID
		announcements.removeIf(announcement -> announcements.stream()
				.anyMatch(c -> c != announcement && c.getId().equals(announcement.getId())));
		return announcements;
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(AnnouncementTemplate.class);
	}

	private List<Announcement> addAnnouncementsFromConfig(Path path) {
		AnnouncementsConfig config = Config.update(path, AnnouncementsConfig.class);
		return config.getAnnouncements().stream()
				.filter(Announcement::isEnabled)
				.toList();
	}
}