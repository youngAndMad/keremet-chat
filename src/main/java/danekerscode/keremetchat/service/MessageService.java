package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.notification.MessageNotification;

public interface MessageService {

    MessageNotification saveMessage(User sender, Long chatId, MessageRequest messageRequest);

}
