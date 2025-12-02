package me.whereareiam.socialismus.module.chirper.common.config.template;

import com.google.inject.Singleton;
import me.whereareiam.commandant.model.CommandDefinition;
import me.whereareiam.configura.TemplateProvider;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;

import java.util.List;
import java.util.Map;

@Singleton
public class ChirperCommandsTemplate implements TemplateProvider<ChirperCommands> {
	@Override
	public ChirperCommands supply(ChirperCommands config) {
		// Default values
		CommandDefinition announce = CommandDefinition.builder()
				.enabled(true)
				.aliases(List.of("announce", "broadcast", "chirp"))
				.permission("chirper.admin")
				.description("Announce command")
				.usage("{command} {alias} <id> [simplified]")
				.cooldown(CommandDefinition.Cooldown.builder()
						.enabled(true)
						.duration(2)
						.group("global")
						.build()
				)
				.arguments(Map.of(
						"id", "ID",
						"simplified", "Simplify"
				))
				.build();

		config.getCommands().put("announce", announce);

		return config;
	}
}
