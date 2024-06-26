package danekerscode.keremetchat.model.dto.request.websocket;

import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Data transfer object to send notifications through websocket
 * Generic raw type Content means type of expected content in notification
 *
 * @param sender Sender of notification
 * @param <Content> type of content
 * @param content   value of content
 * @param type      type of websocket notification type
 */
public record NotificationDelivery<Content extends Serializable>(
        @NotNull WebsocketNotificationType type,
        @NotNull UserResponseDto sender,
        @NotNull Long chatId,
        @Nullable Content content
) {
}
