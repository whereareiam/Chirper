package me.whereareiam.socialismus.module.chirper.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.common.config.template.ChirperCommandsTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class ChirperCommandsProvider implements Provider<ChirperCommands>, Reloadable {
	private final Path workingPath;
	private ChirperCommands commands;

	@Inject
	public ChirperCommandsProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> reloadableRegistry
	) {
		this.workingPath = workingPath;

		Config.registerTemplate(ChirperCommandsTemplate.class);
		reloadableRegistry.register(this);
	}

	@Override
	public ChirperCommands get() {
		if (commands != null) return commands;
		commands = Config.update(workingPath.resolve("commands"), ChirperCommands.class);
		return commands;
	}

	@Override
	public void reload() {
		commands = Config.update(workingPath.resolve("commands"), ChirperCommands.class);
	}
}
