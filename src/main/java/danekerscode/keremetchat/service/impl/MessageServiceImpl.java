package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.MessageRepository;
import danekerscode.keremetchat.service.ChatService;
import danekerscode.keremetchat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatService chatService;

    @Override
    public void saveMessage(
            User sender,
            Long chatId,
            MessageRequest messageRequest
    ) {
        var chatExists = chatService.existsById(chatId);

        if (!chatExists) {
            throw new EntityNotFoundException(Chat.class, chatId);
        }

        var chatMemberUserIdList = chatService.getMemberUsersIdList(chatId);

        if (!chatMemberUserIdList.contains(sender.getId())) {
            throw new IllegalArgumentException("User is not a member of the chat"); //todo custom exception
        }


        //todo save message
    }
}
