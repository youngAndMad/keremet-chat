package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.mapper.MessageMapper;
import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import danekerscode.keremetchat.model.notification.MessageNotification;
import danekerscode.keremetchat.repository.MessageRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatService;
import danekerscode.keremetchat.service.FileStorageService;
import danekerscode.keremetchat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatMemberService chatMemberService;
    private final MessageMapper messageMapper;
    private final ChatService chatService;
    private final FileStorageService fileStorageService;

    @Transactional
    @Override
    public MessageNotification saveMessage(
            User currentUser,
            Long chatId,
            MessageRequest messageRequest
    ) {
        var chat = chatService.findById(chatId);

        var chatMemberId = chatMemberService
                .findByChatIdAndUserId(chatId, currentUser.getId())
                .orElseThrow(() -> new InvalidRequestPayloadException("User is not a member of the chat"));

        var messageId = messageRepository.insertMessage(messageRequest.content(), chatId, chatMemberId, messageRequest.parentId());

        var fileEntities = new ArrayList<FileEntity>();

        if (!CollectionUtils.isEmpty(messageRequest.files())) {
            messageRequest.files().stream()
                    .map(file -> fileStorageService.save(file, FileEntitySource.MESSAGE_CONTENT, String.valueOf(messageId)))
                    .forEach(fileEntity -> {
                        messageRepository.insertMessageFile(messageId, fileEntity.getId());
                        fileEntities.add(fileEntity);
                    });
        }

        return messageMapper.toNotification(currentUser, chat, messageRequest, fileEntities);
    }
}
