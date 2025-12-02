package me.whereareiam.socialismus.module.chirper.common.config.template;

import com.google.inject.Singleton;
import me.whereareiam.configura.TemplateProvider;
import me.whereareiam.socialismus.module.chirper.api.model.config.ChirperMessages;

@Singleton
public class ChirperMessagesTemplate implements TemplateProvider<ChirperMessages> {
	@Override
	public ChirperMessages supply(ChirperMessages config) {
		// Default values
		config.setNoPlayers("{prefix}<white>There are no players online to send the announcement to.");
		config.setNoAnnouncementFound("{prefix}<white>Announcement with id <red>{id}<white> not found.");
		config.setAnnouncementBroadcasted("{prefix}<white>Announcement <red>{id}<white> has been broadcasted.");

		return config;
	}
}
