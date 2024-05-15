package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;
import danekerscode.keremetchat.model.notification.CommonStopChatNotificationRequest;
import danekerscode.keremetchat.service.ChatNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public final class NotificationController extends AbstractWebSocketController {

    private final ChatNotificationService chatNotificationService;

    @MessageMapping("/notification/common/chat/{chatId}")
    void deliverNotification(
            @DestinationVariable Long chatId,
            Authentication auth,
            @Payload @Validated CommonChatNotificationRequest notificationRequest
    ) {
        var currentUser = super.getUserResponseDtoFromAuth(auth);

        var chatNotification = chatNotificationService
                .saveCommonNotification(notificationRequest, currentUser, chatId);

        var onlineChatMembers = new ArrayList<Long>();
        var offlineChatMembers = new ArrayList<Long>();

        super.findChatMemberUsersId(chatId)
                .stream().map(super::getUserActivity)
                .forEach(userActivity -> {
                    if (userActivity.isOnline()) {
                        onlineChatMembers.add(userActivity.getUserId());
                    } else {
                        offlineChatMembers.add(userActivity.getUserId());
                    }
                });

        super.deliverWebSocketMessage(chatNotification, WebSocketDestination.LIVE_USER_NOTIFICATION, onlineChatMembers.toArray(Long[]::new));


    }

    @MessageMapping("/notification/common/chat/stop/{chatId}")
    void stopNotification(
            @DestinationVariable Long chatId,
            Authentication auth,
            @Payload @Validated CommonStopChatNotificationRequest request
    ) {
        var currentUser = super.getUserResponseDtoFromAuth(auth);

        chatNotificationService.deleteNotification(request, currentUser, chatId);
    }

}
