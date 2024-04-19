package danekerscode.keremetchat.model.dto.request.websocket;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeliverNotificationRequest(
        @NotNull @Positive Long chatId,
        @NotNull WebsocketNotificationType type
) {
}
