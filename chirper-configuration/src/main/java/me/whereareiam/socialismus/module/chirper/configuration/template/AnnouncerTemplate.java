package me.whereareiam.socialismus.module.chirper.configuration.template;

import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.chirper.api.model.announcer.Announcer;
import me.whereareiam.socialismus.module.chirper.api.type.OrderType;
import me.whereareiam.socialismus.module.chirper.configuration.dynamic.AnnouncersConfig;

import java.util.List;

public class AnnouncerTemplate implements DefaultConfig<AnnouncersConfig> {
    @Override
    public AnnouncersConfig getDefault() {
        AnnouncersConfig config = new AnnouncersConfig();

        Announcer example = Announcer.builder()
                .enabled(true)
                .announcements(List.of(
                        "example0",
                        "bossBarExample0",
                        "bossBarExample1",
                        "bossBarExample2",
                        "titleExample0",
                        "titleExample1"
                ))
                .settings(Announcer.AnnouncerSettings.builder()
                        .interval(300)
                        .delay(0)
                        .order(OrderType.DESCENDING)
                        .build())
                .build();

        config.getAnnouncers().add(example);

        return config;
    }
}
