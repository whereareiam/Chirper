package me.whereareiam.socialismus.module.chirper.common;

import com.google.inject.AbstractModule;
import me.whereareiam.socialismus.module.chirper.api.input.AnnouncementBroadcaster;
import me.whereareiam.socialismus.module.chirper.common.broadcast.BroadcastCoordinator;

public class CommonConfiguration extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnnouncementBroadcaster.class).to(BroadcastCoordinator.class);
    }
}
