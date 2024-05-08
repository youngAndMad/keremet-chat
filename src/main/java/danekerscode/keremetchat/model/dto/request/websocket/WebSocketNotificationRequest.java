package danekerscode.keremetchat.model.dto.request.websocket;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import org.springframework.lang.NonNull;

public record WebSocketNotificationRequest(
        @NonNull WebsocketNotificationType type
) {
}
