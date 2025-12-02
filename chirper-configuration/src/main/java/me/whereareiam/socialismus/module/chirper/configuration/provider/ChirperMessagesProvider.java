package me.whereareiam.socialismus.module.chirper.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import me.whereareiam.socialismus.module.chirper.configuration.template.ChirperMessagesTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class ChirperMessagesProvider implements Provider<ChirperMessages>, Reloadable {
	private final Path workingPath;
	private ChirperMessages messages;

	@Inject
	public ChirperMessagesProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> reloadableRegistry
	) {
		this.workingPath = workingPath;

		Config.registerTemplate(ChirperMessagesTemplate.class);
		reloadableRegistry.register(this);
	}

	@Override
	public ChirperMessages get() {
		if (messages != null) return messages;
		messages = Config.update(workingPath.resolve("messages"), ChirperMessages.class);
		return messages;
	}

	@Override
	public void reload() {
		messages = Config.update(workingPath.resolve("messages"), ChirperMessages.class);
	}
}
