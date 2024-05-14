package danekerscode.keremetchat.core.mapper;

import danekerscode.keremetchat.model.dto.ChatSettingsDto;
import danekerscode.keremetchat.model.entity.ChatSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ChatSettingsMapper {

    void fetchDtoFields(
            @MappingTarget ChatSettings chatSettings,
            ChatSettingsDto chatSettingsDto
    );
}
