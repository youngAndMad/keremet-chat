package danekerscode.keremetchat.core.mapper;

import danekerscode.keremetchat.model.dto.ChatSettingsDto;
import danekerscode.keremetchat.model.entity.ChatSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ChatSettingsMapper {

    void fetchDtoFields(
            @MappingTarget ChatSettings chatSettings,
            ChatSettingsDto chatSettingsDto
    );

    @Mapping(target = "everyoneCanInviteMembers" , constant = "true")
    @Mapping(target = "membersListIsAvailable" , constant = "true")
    @Mapping(target = "adminsCanEditSettings" , constant = "false")
    void resetToDefault(@MappingTarget ChatSettings chatSettings);
}
