package me.whereareiam.socialismus.module.chirper.common;

import com.google.inject.AbstractModule;
import me.whereareiam.socialismus.module.chirper.api.AnnouncementBroadcaster;
import me.whereareiam.socialismus.module.chirper.common.broadcast.BroadcastCoordinator;

public class CommonConfiguration extends AbstractModule {
	@Override
	protected void configure() {
		// Bind broadcaster
		bind(AnnouncementBroadcaster.class).to(BroadcastCoordinator.class);
	}
}
