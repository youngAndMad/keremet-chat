package danekerscode.keremetchat.model.notification;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.validation.constraints.NotNull;

public record CommonChatNotificationRequest(
        @NotNull WebsocketNotificationType type
) {
}
