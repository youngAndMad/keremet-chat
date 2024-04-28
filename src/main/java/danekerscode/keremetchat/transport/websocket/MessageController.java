package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.service.MessageService;
import danekerscode.keremetchat.service.UserNotificationService;
import danekerscode.keremetchat.service.UserStatusService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Controller
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

        messageService.saveMessage(user, chatId, messageRequest);

        var chatMembers = super.findChatMemberUsersId(chatId);

        chatMembers.stream()
                .map(userStatusService::getUserActivity)
                .forEach(userActivity -> {
                    if (!userActivity.isOnline()) {
                        // todo send real time message with web socket
                    } else {
//                        userNotificationService.save() todo save notification for user
                    }
                });
    }

}
