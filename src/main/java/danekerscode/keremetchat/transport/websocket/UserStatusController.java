package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/status/")
public final class UserStatusController extends AbstractWebSocketController{

    private final UserStatusService userStatusService;
    private final SimpMessagingTemplate ws;

    @MessageMapping("start-session")
    void startUserSession(Authentication auth) {
        var user = super.getUserFromAuthentication(auth);
        userStatusService.setOnlineStatus(user.getId());
    }

    @MessageMapping("close-session")
    void closeUserSession(Authentication auth) {
        var user = super.getUserFromAuthentication(auth);
        userStatusService.setOfflineStatus(user.getId());
    }


}
