package me.whereareiam.socialismus.module.chirper.api.model.config;

import lombok.Getter;
import lombok.ToString;
import me.whereareiam.socialismus.api.model.CommandEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class ChirperCommands {
    private Map<String, CommandEntity> commands = new HashMap<>();
}
