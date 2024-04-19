package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.dto.request.websocket.DeliverNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
public final class NotificationController extends AbstractWebSocketController{


    @MessageMapping("/notification/send")
    void deliverNotification(
            @Payload @Validated DeliverNotificationRequest notificationRequest
    ) {

    }
}
