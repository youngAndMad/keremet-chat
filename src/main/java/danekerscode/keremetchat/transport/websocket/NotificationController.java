package danekerscode.keremetchat.transport.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public final class NotificationController extends AbstractWebSocketController {

    @MessageMapping("/notification/send")
    void deliverNotification(
            Authentication auth
    ) {
        var currentUser = super.getUserFromAuthentication(auth);

//        var chatMembers = super.findChatMemberUsersId(chatId); todo
//
//        chatMembers.stream().map(super::getUserActivity)
//                .filter(UserActivity::isOnline)
//                .forEach();
    }
}
