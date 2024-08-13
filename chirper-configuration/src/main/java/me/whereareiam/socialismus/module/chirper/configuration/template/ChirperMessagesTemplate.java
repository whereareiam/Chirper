package me.whereareiam.socialismus.module.chirper.configuration.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;

@Singleton
public class ChirperMessagesTemplate implements DefaultConfig<ChirperMessages> {
    @Override
    public ChirperMessages getDefault() {
        ChirperMessages config = new ChirperMessages();

        // Default values
        config.setNoPlayers("{prefix}<white>There are no players online to send the announcement to.");
        config.setNoAnnouncementFound("{prefix}<white>Announcement with id <red>{id}<white> not found.");

        config.setAnnouncementBroadcasted("{prefix}<white>Announcement <red>{id}<white> has been broadcasted.");

        return config;
    }
}
