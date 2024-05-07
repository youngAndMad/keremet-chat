package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import danekerscode.keremetchat.service.UserNotificationService;
import danekerscode.keremetchat.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public final class UserStatusController extends AbstractWebSocketController {

    private final UserStatusService userStatusService;
    private final UserNotificationService userNotificationService;

    @MessageMapping("/user/status/start-session")
    void startUserSession(Authentication auth) {
        var user = super.getUserFromAuthentication(auth);
        userStatusService.setOnlineStatus(user.getId());

        var userNotifications = userNotificationService.getUserNotifications(user.getId());

        userNotifications.forEach(userNotification -> {

            super.deliverWebSocketMessage(userNotification, WebSocketDestination.USER_NOTIFICATION, user.getId());
        });
    }

    @MessageMapping("/user/status/close-session")
    void closeUserSession(Authentication auth) {
        var user = super.getUserFromAuthentication(auth);
        userStatusService.setOfflineStatus(user.getId());
    }


}
