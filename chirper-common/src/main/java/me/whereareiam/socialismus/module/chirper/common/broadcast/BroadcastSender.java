package me.whereareiam.socialismus.module.chirper.common.broadcast;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.serializer.SerializationService;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.AnnouncementContent;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.variant.*;
import me.whereareiam.socialismus.module.chirper.api.type.AnnouncementType;
import me.whereareiam.socialismus.module.chirper.common.util.BossBarUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Singleton
public class BroadcastSender {
    private final SerializationService serializationService;
    private final BossBarUtil bossBarUtil;

    @Inject
    public BroadcastSender(SerializationService serializationService, BossBarUtil bossBarUtil) {
        this.serializationService = serializationService;
        this.bossBarUtil = bossBarUtil;
    }

    public void sendContent(AnnouncementType type, AnnouncementContent content, DummyPlayer recipient) {
        Audience audience = recipient.getAudience();

        switch (type) {
            case MESSAGE -> audience.sendMessage(
                    serializationService.format(recipient, String.join("\n", ((MessageAnnouncement) content).getMessage()))
            );
            case BOSSBAR -> bossBarUtil.createTemporaryBossBar(
                    recipient,
                    BossBar.bossBar(
                            serializationService.format(recipient, ((BossBarAnnouncement) content).getMessage()),
                            1.0f,
                            ((BossBarAnnouncement) content).getColor(),
                            ((BossBarAnnouncement) content).getOverlay()
                    ),
                    ((BossBarAnnouncement) content).getSettings().getDuration()
            );
            case TITLE -> {
                TitleAnnouncement title = (TitleAnnouncement) content;
                audience.showTitle(Title.title(
                        serializationService.format(recipient, title.getTitle()),
                        serializationService.format(recipient, title.getSubtitle()),
                        Title.Times.times(Duration.of(title.getSettings().getFadeIn(), ChronoUnit.MILLIS),
                                Duration.of(title.getSettings().getStay(), ChronoUnit.MILLIS),
                                Duration.of(title.getSettings().getFadeOut(), ChronoUnit.MILLIS)
                        )
                ));
            }
            case ACTIONBAR -> audience.sendActionBar(
                    serializationService.format(recipient, ((ActionbarAnnouncement) content).getMessage())
            );
            case SOUND -> {
                SoundAnnouncement sound = (SoundAnnouncement) content;
                audience.playSound(Sound.sound(
                        Key.key(sound.getSound()),
                        Sound.Source.MASTER,
                        sound.getSettings().getVolume(),
                        sound.getSettings().getPitch()
                ));
            }
        }
    }
}
