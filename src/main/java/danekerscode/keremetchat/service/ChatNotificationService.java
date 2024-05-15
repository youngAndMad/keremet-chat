package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.notification.ChatNotification;
import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;
import danekerscode.keremetchat.model.notification.CommonStopChatNotificationRequest;

public interface ChatNotificationService {

    ChatNotification saveCommonNotification(
            CommonChatNotificationRequest request,
            UserResponseDto currentUser,
            Long chatId
    );

    void cascadeForChat(Long chatId);

    Long lastNotificationInnerId(Long chatId);

    void deleteNotification(CommonStopChatNotificationRequest request, UserResponseDto currentUser, Long chatId);

}
