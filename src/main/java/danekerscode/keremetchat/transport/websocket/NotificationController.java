package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.dto.request.websocket.WebSocketNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public final class NotificationController extends AbstractWebSocketController {

    @MessageMapping("/notification/chat/{chatId}")
    void deliverNotification(
            @DestinationVariable Long chatId,
            Authentication auth,
            @Payload WebSocketNotificationRequest notificationRequest
    ) {
        var currentUser = super.getUserFromAuthentication(auth);


    }
}
