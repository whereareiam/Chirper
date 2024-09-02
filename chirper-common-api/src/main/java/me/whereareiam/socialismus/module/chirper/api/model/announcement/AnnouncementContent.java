package me.whereareiam.socialismus.module.chirper.api.model.announcement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.whereareiam.socialismus.api.model.requirement.RequirementGroup;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AnnouncementContent {
    private RequirementGroup requirements;
}
