package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.common.annotation.FetchUserContext;
import danekerscode.keremetchat.context.UserContextHelper;
import danekerscode.keremetchat.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;
    private final UserContextHelper userContextHelper;
    private final SimpMessagingTemplate ws;

    @MessageMapping("/user/status/start-session")
    void startUserSession(Authentication auth) {
        var user = userContextHelper.extractUser(auth);
        userStatusService.setOnlineStatus(user.getId());
    }

    @MessageMapping("/user/status/close-session")
    void closeUserSession(Authentication auth) {
        var user = userContextHelper.extractUser(auth);
        userStatusService.setOfflineStatus(user.getId());
    }


}
