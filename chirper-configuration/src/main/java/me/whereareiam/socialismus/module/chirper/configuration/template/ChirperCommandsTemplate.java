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
                .aliases(List.of("announce", "broadcast", "chirp"))
                .permission("socialismus.admin")
                .description("Announce command")
                .usage("{command} {alias} <id> [bool]")
                .enabled(true)
                .build();

        config.getCommands().put("announce", announce);

        return config;
    }
}
