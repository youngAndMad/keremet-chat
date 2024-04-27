package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import danekerscode.keremetchat.repository.MessageRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatService;
import danekerscode.keremetchat.service.FileStorageService;
import danekerscode.keremetchat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatMemberService chatMemberService;
    private final ChatService chatService;
    private final FileStorageService fileStorageService;

    @Transactional
    @Override
    public void saveMessage(
            User currentUser,
            Long chatId,
            MessageRequest messageRequest
    ) {
        var chatExists = chatService.existsById(chatId);

        if (!chatExists) {
            throw new EntityNotFoundException(Chat.class, chatId);
        }

        var chatMemberId = chatMemberService.findByChatIdAndUserId(chatId, currentUser.getId())
                .orElseThrow(() -> new InvalidRequestPayloadException("User is not a member of the chat"));

        var messageId = messageRepository.insertMessage(messageRequest.content(), chatId, chatMemberId, messageRequest.parentId());

        if (CollectionUtils.isEmpty(messageRequest.files())) {
            return;
        }

        messageRequest.files().stream()
                .map(file -> fileStorageService.save(file, FileEntitySource.MESSAGE_CONTENT, String.valueOf(messageId)))
                .forEach(fileEntity -> messageRepository.insertMessageFile(messageId, fileEntity.getId()));
    }
}
