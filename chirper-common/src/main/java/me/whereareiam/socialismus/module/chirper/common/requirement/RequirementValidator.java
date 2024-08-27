package me.whereareiam.socialismus.module.chirper.common.requirement;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.RequirementValidation;
import me.whereareiam.socialismus.api.input.registry.ExtendedRegistry;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.model.requirement.Requirement;
import me.whereareiam.socialismus.api.type.requirement.RequirementType;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.Announcement;
import me.whereareiam.socialismus.module.chirper.api.model.announcement.AnnouncementContent;

import java.util.Map;

@Singleton
public class RequirementValidator {
    private final ExtendedRegistry<RequirementType, RequirementValidation> requirementRegistry;

    @Inject
    public RequirementValidator(ExtendedRegistry<RequirementType, RequirementValidation> requirementRegistry) {
        this.requirementRegistry = requirementRegistry;
    }

    public boolean isRequirementMet(Map.Entry<RequirementType, ? extends Requirement> entry, DummyPlayer dummyPlayer) {
        RequirementValidation checker = requirementRegistry.get(entry.getKey());
        if (checker == null)
            return true;

        return !checker.check(entry.getValue(), dummyPlayer);
    }

    public boolean checkRequirements(Announcement announcement, DummyPlayer dummyPlayer) {
        if (announcement.getRequirements().isEmpty()) return true;
        for (Map.Entry<RequirementType, ? extends Requirement> entry : announcement.getRequirements().entrySet())
            if (isRequirementMet(entry, dummyPlayer))
                return false;

        return true;
    }

    public boolean checkRequirements(AnnouncementContent announcementContent, DummyPlayer dummyPlayer) {
        if (announcementContent.getRequirements().isEmpty()) return true;
        for (Map.Entry<RequirementType, ? extends Requirement> entry : announcementContent.getRequirements().entrySet())
            if (isRequirementMet(entry, dummyPlayer))
                return false;

        return true;
    }
}
