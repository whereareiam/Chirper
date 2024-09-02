package me.whereareiam.socialismus.module.chirper.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;

import java.util.List;

@Singleton
public class ChirperCommandsTemplate implements DefaultConfig<ChirperCommands> {
    @Override
    public ChirperCommands getDefault() {
        ChirperCommands config = new ChirperCommands();

        // Default values
        CommandEntity announce = CommandEntity.builder()
                .enabled(true)
                .aliases(List.of("announce", "broadcast", "chirp"))
                .permission("socialismus.admin")
                .description("Announce command")
                .usage("{command} {alias} <id> [bool]")
                .cooldown(CommandEntity.Cooldown.builder()
                        .enabled(true)
                        .duration(2)
                        .group("global")
                        .build()
                ).build();

        config.getCommands().put("announce", announce);

        return config;
    }
}
