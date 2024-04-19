package danekerscode.keremetchat.model.dto.request.websocket;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Data transfer object to send notifications through websocket
 * Generic raw type Content means type of expected content in notification
 *
 * @param identifier identifier of receiver, will be null if type is WebsocketNotificationType.GLOBAL
 * @param <Content> type of content
 * @param content value of content
 * @param type type of websocket notification type
 */
public record DeliverNotificationRequest<Content extends Serializable>(
        @Nullable Long identifier,
        @NotNull WebsocketNotificationType type,
        @Nullable Content content
) {
}