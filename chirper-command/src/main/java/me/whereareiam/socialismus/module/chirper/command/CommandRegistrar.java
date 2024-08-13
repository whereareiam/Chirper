package me.whereareiam.socialismus.module.chirper.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.command.executor.AnnounceCommand;

import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class CommandRegistrar {
    private final Injector injector;
    private final CommandService commandService;
    private final Provider<ChirperCommands> commands;
    private final Registry<Map<String, CommandEntity>> commandRegistry;

    @Inject
    public CommandRegistrar(Injector injector, CommandService commandService, Provider<ChirperCommands> commands, Registry<Map<String, CommandEntity>> commandRegistry) {
        this.injector = injector;
        this.commandService = commandService;
        this.commands = commands;
        this.commandRegistry = commandRegistry;
    }

    public void registerCommands() {
        commandRegistry.register(commands.get().getCommands());

        Stream.of(
                injector.getInstance(AnnounceCommand.class)
        ).forEach(commandService::registerCommand);
    }
}
