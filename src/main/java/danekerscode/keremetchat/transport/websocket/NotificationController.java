package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;
import danekerscode.keremetchat.service.ChatNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public final class NotificationController extends AbstractWebSocketController {

    private final ChatNotificationService chatNotificationService;

    @MessageMapping("/notification/common/chat/{chatId}")
    void deliverNotification(
            @DestinationVariable Long chatId,
            Authentication auth,
            @Payload CommonChatNotificationRequest notificationRequest
    ) {
        var currentUser = super.getUserResponseDtoFromAuth(auth);


    }
}
