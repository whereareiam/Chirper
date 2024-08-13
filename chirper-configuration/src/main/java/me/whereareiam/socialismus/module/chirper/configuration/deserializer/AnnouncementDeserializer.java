package me.whereareiam.socialismus.module.chirper.configuration.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.AnnouncementContent;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.variant.*;

import java.io.IOException;

@Singleton
public class AnnouncementDeserializer extends JsonDeserializer<AnnouncementContent> {
    @Override
    public AnnouncementContent deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = parser.getCodec();
        JsonNode root = codec.readTree(parser);

        if (root.has("message") && root.has("overlay") && root.has("color"))
            return codec.treeToValue(root, BossBarAnnouncement.class);

        if (root.has("message") && root.get("message").isArray())
            return codec.treeToValue(root, MessageAnnouncement.class);

        if (root.has("title") && root.has("subtitle"))
            return codec.treeToValue(root, TitleAnnouncement.class);

        if (root.has("message"))
            return codec.treeToValue(root, ActionbarAnnouncement.class);

        if (root.has("sound"))
            return codec.treeToValue(root, SoundAnnouncement.class);

        return null;
    }
}