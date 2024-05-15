package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.ChatSettingsDto;
import danekerscode.keremetchat.model.entity.User;

public interface ChatSettingsService {

    void updateSettingsFor(Long chatId, ChatSettingsDto chatSettingsDto, User currentUser);

    void setDefaultForChat(Long id,User currentUser);
}
