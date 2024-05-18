package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;
import danekerscode.keremetchat.model.notification.CommonStopChatNotificationRequest;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
public final class NotificationController extends AbstractWebSocketController {

    private final ChatNotificationService chatNotificationService;
    private final ChatMemberService chatMemberService;

    @MessageMapping("/notification/common/chat/{chatId}")
    void deliverNotification(
            @DestinationVariable Long chatId,
            Authentication auth,
            @Payload @Validated CommonChatNotificationRequest notificationRequest
    ) {
        var currentUser = super.getUserResponseDtoFromAuth(auth);

        var chatNotification = chatNotificationService
                .saveCommonNotification(notificationRequest, currentUser, chatId);

        super.deliverWebSocketMessage(chatNotification, WebSocketDestination.START_LIVE_USER_NOTIFICATION, chatId);

        super.findChatMemberUsersId(chatId)
                .stream()
                .map(super::getUserActivity)
                .filter(UserActivity::isOnline)
                .map(userActivity -> super.findMemberByUserAndChat(userActivity.getUserId(), chatId))
                .forEach(chatMember -> chatMemberService.updateLastNotificationState(chatMember.getId(), chatNotification.getId()));
    }

    @MessageMapping("/notification/common/chat/stop/{chatId}")
    void stopNotification(
            @DestinationVariable Long chatId,
            Authentication auth,
            @Payload @Validated CommonStopChatNotificationRequest request
    ) {
        var currentUser = super.getUserResponseDtoFromAuth(auth);

//        chatNotificationService.findBySenderAndType()

        chatNotificationService.deleteNotification(request, currentUser, chatId);
    }

}
