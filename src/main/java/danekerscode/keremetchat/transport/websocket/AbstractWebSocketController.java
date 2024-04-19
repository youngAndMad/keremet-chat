package danekerscode.keremetchat.transport.websocket;


import danekerscode.keremetchat.common.helper.UserContextHelper;
import danekerscode.keremetchat.common.helper.WebSocketMessagingHelper;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.websocket.WebSocketDestinationType;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Setter
public sealed abstract class AbstractWebSocketController permits
    MessageController,NotificationController,UserStatusController {

    private UserContextHelper userContextHelper;
    private WebSocketMessagingHelper webSocketMessagingHelper;

    protected User getUserFromAuthentication(Authentication auth) {
        return userContextHelper.extractUser(auth);
    }

    protected void deliverWebSocketMessage(
            Object data,
            WebSocketDestinationType webSocketDestinationType,
            Long... destinationIdentifiers
    ){
        this.webSocketMessagingHelper.deliver(data,webSocketDestinationType,destinationIdentifiers);
    }


}
