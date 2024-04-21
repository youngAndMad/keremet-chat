package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.User;

public interface MessageService {

    void saveMessage(User sender, Long chatId, MessageRequest messageRequest);

}
