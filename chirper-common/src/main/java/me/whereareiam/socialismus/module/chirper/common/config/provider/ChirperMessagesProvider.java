package me.whereareiam.socialismus.module.chirper.common.config.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import me.whereareiam.socialismus.module.chirper.common.config.ChirperConfigProvider;
import me.whereareiam.socialismus.module.chirper.common.config.template.ChirperMessagesTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class ChirperMessagesProvider extends ChirperConfigProvider<ChirperMessages> {
	@Inject
	public ChirperMessagesProvider(
			@Named("workingPath") Path workingPath,
			Registry<Reloadable> registry
	) {
		super(workingPath, registry);
	}

	@Override
	protected ChirperMessages load() {
		return Config.update(getBasePath().resolve("messages"), ChirperMessages.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(ChirperMessagesTemplate.class);
	}
}
