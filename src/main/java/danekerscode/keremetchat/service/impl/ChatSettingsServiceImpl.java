package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.mapper.ChatSettingsMapper;
import danekerscode.keremetchat.model.dto.ChatSettingsDto;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.ChatSettings;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import danekerscode.keremetchat.repository.ChatSettingsRepository;
import danekerscode.keremetchat.service.ChatSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatSettingsServiceImpl implements ChatSettingsService {

    private final ChatSettingsRepository chatSettingsRepository;
    private final ChatSettingsMapper chatSettingsMapper;

    @Override
    public void updateSettingsFor(Long chatId, ChatSettingsDto chatSettingsDto, User currentUser) {
        var chatSettings = getChatSettingsOrThrow(chatId);
        var currentMember = chatSettings.getChat().memberForUser(currentUser.getId());

        requirePermissionToEditSettings(currentMember, chatSettings);

        chatSettingsMapper.fetchDtoFields(chatSettings, chatSettingsDto);
        chatSettingsRepository.save(chatSettings);
    }

    @Override
    public void setDefaultForChat(Long chatId, User currentUser) {
        var chatSettings = getChatSettingsOrThrow(chatId);
        var currentMember = chatSettings.getChat().memberForUser(currentUser.getId());

        requirePermissionToEditSettings(currentMember, chatSettings);

        chatSettingsMapper.resetToDefault(chatSettings);
        chatSettingsRepository.save(chatSettings);
    }

    private ChatSettings getChatSettingsOrThrow(Long chatId) {
        return chatSettingsRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(Chat.class, chatId));
    }

    private void requirePermissionToEditSettings(ChatMember currentMember, ChatSettings chatSettings) {
        if (currentMember.isMember() ||
                (currentMember.getRole() == ChatUserRole.ADMIN && !chatSettings.getAdminsCanEditSettings())) {
            throw new InvalidRequestPayloadException("You are not allowed to edit chat settings. Permission denied");
        }
    }

}
