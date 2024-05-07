package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.UserNotification;
import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import danekerscode.keremetchat.service.MessageService;
import danekerscode.keremetchat.service.UserNotificationService;
import danekerscode.keremetchat.service.UserStatusService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Controller
@Slf4j
public final class MessageController extends AbstractWebSocketController {

    private final MessageService messageService;
    private final UserStatusService userStatusService;
    private final UserNotificationService userNotificationService;

    @MessageMapping("/message/{chatId}")
    void sendMessage(
            @DestinationVariable @Positive Long chatId,
            Authentication auth,
            @Validated @Payload MessageRequest messageRequest
    ) {
        var user = super.getUserFromAuthentication(auth);

        var messageNotification = messageService.saveMessage(user, chatId, messageRequest);

        var chatMembers = super.findChatMemberUsersId(chatId);

        chatMembers.stream()
                .map(userStatusService::getUserActivity)
                .forEach(userActivity -> {
                    var notification = new UserNotification<>(
                            userActivity.getUserId(),
                            WebsocketNotificationType.MESSAGE,
                            messageNotification
                    );
                    if (!userActivity.isOnline()) {
                        userNotificationService.save(notification);
                    } else {
                        super.deliverWebSocketMessage(notification, WebSocketDestination.USER_NOTIFICATION, userActivity.getUserId());
                    }
                });
    }

}
