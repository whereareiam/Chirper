package me.whereareiam.socialismus.module.chirper.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.common.config.ChirperConfigProvider;
import me.whereareiam.socialismus.module.chirper.common.config.template.ChirperCommandsTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class ChirperCommandsProvider extends ChirperConfigProvider<ChirperCommands> {
	@Inject
	public ChirperCommandsProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> registry
	) {
		super(workingPath, registry);
	}

	@Override
	protected ChirperCommands load() {
		return Config.update(getBasePath().resolve("commands"), ChirperCommands.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(ChirperCommandsTemplate.class);
	}
}
