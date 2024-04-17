package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.model.dto.request.UserWebSocketSessionRequest;
import danekerscode.keremetchat.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    @MessageMapping("/user/status/start-session")
    void startUserSession(
            UserWebSocketSessionRequest userWebSocketSessionRequest
    ){
        userStatusService.setOnlineStatus(userWebSocketSessionRequest.userId());
    }

    @MessageMapping("/user/status/close-session")
    void closeUserSession(
            UserWebSocketSessionRequest userWebSocketSessionRequest
    ){
        userStatusService.setOfflineStatus(userWebSocketSessionRequest.userId());
    }


}
