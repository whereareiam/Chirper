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
import me.whereareiam.socialismus.module.chirper.api.input.AnnouncementBroadcaster;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperCommands;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
@Command("%command.main")
public class AnnounceCommand implements CommandBase {
    private final SerializationService serializer;
    private final AnnouncementBroadcaster broadcaster;

    private final Provider<ChirperCommands> commands;
    private final Provider<ChirperMessages> messages;
    private final Provider<List<Announcement>> announcements;

    @Inject
    public AnnounceCommand(SerializationService serializer, AnnouncementBroadcaster broadcaster, Provider<ChirperCommands> commands, Provider<ChirperMessages> messages,
                           Provider<List<Announcement>> announcements) {
        this.serializer = serializer;
        this.broadcaster = broadcaster;

        this.commands = commands;
        this.messages = messages;
        this.announcements = announcements;
    }

    @Command("%command.announce")
    @CommandDescription("%description.announce")
    @Permission("%permission.announce")
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
    public Map<String, String> getTranslations() {
        CommandEntity command = commands.get().getCommands().get("announce");

        return Map.of(
                "command." + command.getAliases().getFirst() + ".name", command.getUsage().replace("{command}", String.join("|", command.getAliases())),
                "command." + command.getAliases().getFirst() + ".permission", command.getPermission(),
                "command." + command.getAliases().getFirst() + ".description", command.getDescription(),
                "command." + command.getAliases().getFirst() + ".usage", command.getUsage()
        );
    }
}