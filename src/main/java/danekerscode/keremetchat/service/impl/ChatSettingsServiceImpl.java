package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.mapper.ChatSettingsMapper;
import danekerscode.keremetchat.model.dto.ChatSettingsDto;
import danekerscode.keremetchat.model.entity.Chat;
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
    public void updateSettingsFor(
            Long chatId,
            ChatSettingsDto chatSettingsDto,
            User currentUser
    ) {
        var chatSettings = chatSettingsRepository
                .findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(Chat.class, chatId));

        var chat = chatSettings.getChat();

        var currentMember = chat.memberForUser(currentUser.getId());

        if (currentMember.isMember() ||
                (currentMember.getRole() == ChatUserRole.ADMIN && !chatSettings.getAdminsCanEditSettings())) {
            throw new InvalidRequestPayloadException("You are not allowed to edit chat settings. Permission denied");
        }

        chatSettingsMapper.fetchDtoFields(chatSettings, chatSettingsDto);
        chatSettingsRepository.save(chatSettings);
    }
}
