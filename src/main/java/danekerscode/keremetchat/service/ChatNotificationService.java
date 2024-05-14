package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.notification.ChatNotification;
import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;

public interface ChatNotificationService {

    ChatNotification saveCommonNotification(
            CommonChatNotificationRequest request,
            UserResponseDto currentUser,
            Long chatId
    );

    void cascadeForChat(Long chatId);

}
