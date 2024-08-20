package me.whereareiam.socialismus.module.chirper.common.broadcast;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.model.scheduler.DelayedRunnableTask;
import me.whereareiam.socialismus.api.output.Scheduler;
import me.whereareiam.socialismus.module.chirper.api.input.AnnouncementBroadcaster;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.AnnouncementContent;
import me.whereareiam.socialismus.module.chirper.api.type.AnnouncementType;
import me.whereareiam.socialismus.module.chirper.common.requirement.RequirementValidator;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class BroadcastCoordinator implements AnnouncementBroadcaster {
    private final Scheduler scheduler;
    private final PlayerContainerService playerContainer;
    private final BroadcastSender broadcastSender;
    private final RequirementValidator requirementValidator;

    @Inject
    public BroadcastCoordinator(Scheduler scheduler, PlayerContainerService playerContainer, BroadcastSender broadcastSender, RequirementValidator requirementValidator) {
        this.scheduler = scheduler;
        this.playerContainer = playerContainer;
        this.broadcastSender = broadcastSender;
        this.requirementValidator = requirementValidator;
    }

    @Override
    public void broadcast(Announcement announcement) {
        final Random random = new Random();

        Set<DummyPlayer> recipients = playerContainer.getPlayers();
        recipients = recipients.parallelStream()
                .filter(r -> requirementValidator.checkRequirements(announcement, r))
                .collect(Collectors.toSet());

        if (announcement.getSettings().getDelay() > 0) {
            Set<DummyPlayer> finalRecipients = recipients;
            scheduler.schedule(DelayedRunnableTask.builder()
                    .id(random.nextInt())
                    .module("chirper")
                    .delay(announcement.getSettings().getDelay() * 1000L)
                    .runnable(() -> sendAnnouncement(announcement, finalRecipients))
                    .build());

            return;
        }

        sendAnnouncement(announcement, recipients);
    }

    @Override
    public void broadcast(Announcement announcement, boolean simplified) {
        if (!simplified) {
            broadcast(announcement);
            return;
        }

        Set<DummyPlayer> recipients = playerContainer.getPlayers();
        sendAnnouncement(announcement, recipients);
    }

    private void sendAnnouncement(Announcement announcement, Set<DummyPlayer> recipients) {
        for (DummyPlayer recipient : recipients) {
            for (Map.Entry<AnnouncementType, ? extends AnnouncementContent> entry : announcement.getContents().entrySet())
                if (requirementValidator.checkRequirements(entry.getValue(), recipient))
                    broadcastSender.sendContent(entry.getKey(), entry.getValue(), recipient);
        }
    }
}
