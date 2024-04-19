package danekerscode.keremetchat.transport.websocket;


import danekerscode.keremetchat.common.helper.UserContextHelper;
import danekerscode.keremetchat.common.helper.WebSocketMessagingHelper;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public sealed abstract class AbstractWebSocketController permits
    MessageController,NotificationController,UserStatusController {

    @Autowired
    private UserContextHelper userContextHelper;
    @Autowired
    private WebSocketMessagingHelper webSocketMessagingHelper;

    protected User getUserFromAuthentication(Authentication auth) {
        return userContextHelper.extractUser(auth);
    }

    protected void deliverWebSocketMessage(
            Object data,
            WebSocketDestination webSocketDestination,
            Long... destinationIdentifiers
    ){
        this.webSocketMessagingHelper
                .deliver(data, webSocketDestination,destinationIdentifiers);
    }

}
