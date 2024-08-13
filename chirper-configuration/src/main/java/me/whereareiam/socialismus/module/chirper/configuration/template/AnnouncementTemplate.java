package me.whereareiam.socialismus.module.chirper.configuration.template;

import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.variant.*;
import me.whereareiam.socialismus.module.chirper.api.type.AnnouncementType;
import me.whereareiam.socialismus.module.chirper.configuration.dynamic.AnnouncementsConfig;
import net.kyori.adventure.bossbar.BossBar;

import java.util.List;
import java.util.Map;

public class AnnouncementTemplate implements DefaultConfig<AnnouncementsConfig> {
    @Override
    public AnnouncementsConfig getDefault() {
        AnnouncementsConfig config = new AnnouncementsConfig();

        Announcement example0 = Announcement.builder()
                .id("example0")
                .enabled(true)
                .settings(Announcement.AnnouncementSettings.builder()
                        .delay(0)
                        .repeat(true)
                        .build()
                ).contents(Map.of(AnnouncementType.MESSAGE, MessageAnnouncement.builder()
                        .message(List.of(
                                "",
                                "<gold><bold> Socialismus:</bold></gold>",
                                "<white>  Thanks for installing my plugin!",
                                "<white>  If you have any questions, feel free to ask!",
                                "",
                                "<gray>   Telegram: https://whereareiam.t.me/",
                                "<gray>   Discord: @whereareiam",
                                "",
                                "<gray>  If you like the plugin, please leave a review on the plugin page, it will help me a lot!",
                                ""
                        ))
                        .requirements(Map.of())
                        .build())
                ).requirements(Map.of())
                .build();

        Announcement bossBarExample0 = Announcement.builder()
                .id("bossBarExample0")
                .enabled(true)
                .settings(Announcement.AnnouncementSettings.builder()
                        .delay(0)
                        .repeat(true)
                        .build()
                ).contents(Map.of(AnnouncementType.BOSSBAR, BossBarAnnouncement.builder()
                        .message("<red><bold>Warning</bold>:</red> <white>Low on coffee! Productivity may be affected!")
                        .color(BossBar.Color.RED)
                        .overlay(BossBar.Overlay.NOTCHED_20)
                        .settings(BossBarAnnouncement.BossBarAnnouncementSettings.builder()
                                .duration(5)
                                .breakable(true)
                                .build()
                        ).requirements(Map.of())
                        .build())
                ).requirements(Map.of())
                .build();

        Announcement bossBarExample1 = Announcement.builder()
                .id("bossBarExample1")
                .enabled(true)
                .settings(Announcement.AnnouncementSettings.builder()
                        .delay(0)
                        .repeat(true)
                        .build()
                ).contents(Map.of(
                        AnnouncementType.BOSSBAR, BossBarAnnouncement.builder()
                                .message("<blue><bold>Alert</bold>:</blue> <white>Dance break in <gray>3… 2… 1!")
                                .color(BossBar.Color.BLUE)
                                .overlay(BossBar.Overlay.NOTCHED_20)
                                .settings(BossBarAnnouncement.BossBarAnnouncementSettings.builder()
                                        .duration(3)
                                        .breakable(true)
                                        .build()
                                ).requirements(Map.of())
                                .build(),
                        AnnouncementType.ACTIONBAR, ActionbarAnnouncement.builder()
                                .message("<purple>Don't forget to take a break and stretch!")
                                .requirements(Map.of())
                                .build())
                ).requirements(Map.of())
                .build();

        Announcement bossBarExample2 = Announcement.builder()
                .id("bossBarExample2")
                .enabled(true)
                .settings(Announcement.AnnouncementSettings.builder()
                        .delay(0)
                        .repeat(true)
                        .build()
                ).contents(Map.of(AnnouncementType.BOSSBAR, BossBarAnnouncement.builder()
                        .message("<gray>Hmm... my If-loop is stuck! <red>Help!")
                        .color(BossBar.Color.WHITE)
                        .overlay(BossBar.Overlay.PROGRESS)
                        .settings(BossBarAnnouncement.BossBarAnnouncementSettings.builder()
                                .duration(10)
                                .breakable(true)
                                .build()
                        ).requirements(Map.of())
                        .build())
                ).requirements(Map.of())
                .build();

        Announcement titleExample0 = Announcement.builder()
                .id("titleExample0")
                .enabled(true)
                .settings(Announcement.AnnouncementSettings.builder()
                        .delay(0)
                        .repeat(true)
                        .build()
                )
                .contents(Map.of(AnnouncementType.TITLE, TitleAnnouncement.builder()
                        .title("<gold><bold>Breaking news</bold></gold>")
                        .subtitle("<dark_gray>Plugin developer is out of coffee!")
                        .settings(TitleAnnouncement.TitleAnnouncementSettings.builder()
                                .fadeIn(200)
                                .stay(600)
                                .fadeOut(200)
                                .build()
                        ).requirements(Map.of())
                        .build()))
                .requirements(Map.of())
                .build();

        Announcement titleExample1 = Announcement.builder()
                .id("titleExample1")
                .enabled(true)
                .settings(Announcement.AnnouncementSettings.builder()
                        .delay(0)
                        .repeat(true)
                        .build()
                )
                .contents(Map.of(
                        AnnouncementType.TITLE, TitleAnnouncement.builder()
                                .title("<gold><bold>Breaking news</bold></gold>")
                                .subtitle("<dark_gray>whereareiam is now a neko!")
                                .settings(TitleAnnouncement.TitleAnnouncementSettings.builder()
                                        .fadeIn(200)
                                        .stay(600)
                                        .fadeOut(200)
                                        .build()
                                ).requirements(Map.of())
                                .build(),
                        AnnouncementType.SOUND, SoundAnnouncement.builder()
                                .sound("entity_player_levelup")
                                .settings(SoundAnnouncement.SoundAnnouncementSettings.builder()
                                        .volume(1.0f)
                                        .pitch(1.0f)
                                        .build()
                                ).requirements(Map.of())
                                .build()
                ))
                .requirements(Map.of())
                .build();

        config.getAnnouncements().addAll(List.of(
                example0, bossBarExample0, bossBarExample1,
                bossBarExample2, titleExample0, titleExample1
        ));

        return config;
    }
}