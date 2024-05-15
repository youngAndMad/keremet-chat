package danekerscode.keremetchat.model.notification;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.validation.constraints.NotNull;

public record CommonStopChatNotificationRequest(
        @NotNull WebsocketNotificationType type
) {
}
