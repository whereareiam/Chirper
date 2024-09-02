package me.whereareiam.socialismus.module.chirper.command.executor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.serializer.SerializationService;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.model.serializer.SerializerContent;
import me.whereareiam.socialismus.api.model.serializer.SerializerPlaceholder;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandCooldown;
import me.whereareiam.socialismus.module.chirper.api.input.AnnouncementBroadcaster;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.util.List;
import java.util.Optional;

@Singleton
public class AnnounceCommand extends CommandBase {
    private static final String COMMAND_NAME = "announce";

    private final SerializationService serializer;
    private final AnnouncementBroadcaster broadcaster;

    private final Provider<ChirperCommands> commands;
    private final Provider<ChirperMessages> messages;
    private final Provider<List<Announcement>> announcements;

    @Inject
    public AnnounceCommand(SerializationService serializer, AnnouncementBroadcaster broadcaster, Provider<ChirperCommands> commands, Provider<ChirperMessages> messages,
                           Provider<List<Announcement>> announcements) {
        super(COMMAND_NAME);
        this.serializer = serializer;
        this.broadcaster = broadcaster;

        this.commands = commands;
        this.messages = messages;
        this.announcements = announcements;
    }

    @Command("%command." + COMMAND_NAME)
    @CommandDescription("%description." + COMMAND_NAME)
    @CommandCooldown("%cooldown." + COMMAND_NAME)
    @Permission("%permission." + COMMAND_NAME)
    public void onCommand(DummyPlayer dummyPlayer, @Argument(value = "id") String id, @Argument(value = "bool") boolean simplified) {
        Optional<Announcement> announcement = announcements.get().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();

        if (announcement.isEmpty()) {
            dummyPlayer.getAudience().sendMessage(
                    serializer.format(new SerializerContent(
                            dummyPlayer,
                            List.of(new SerializerPlaceholder("{id}", id)),
                            messages.get().getNoAnnouncementFound()
                    )));
            return;
        }

        dummyPlayer.getAudience().sendMessage(
                serializer.format(new SerializerContent(
                        dummyPlayer,
                        List.of(new SerializerPlaceholder("{id}", id)),
                        messages.get().getAnnouncementBroadcasted()
                )));

        broadcaster.broadcast(announcement.get(), simplified);
    }

    @Override
    public CommandEntity getCommandEntity() {
        return commands.get().getCommands().get(COMMAND_NAME);
    }
}