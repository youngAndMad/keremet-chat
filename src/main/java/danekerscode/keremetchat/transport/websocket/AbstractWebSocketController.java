package danekerscode.keremetchat.transport.websocket;


import danekerscode.keremetchat.context.UserContextHelper;
import danekerscode.keremetchat.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public sealed abstract class AbstractWebSocketController permits
    MessageController,NotificationController,UserStatusController {
    private UserContextHelper userContextHelper;

    @Autowired
    public void setUserContextHelper(UserContextHelper userContextHelper) {
        this.userContextHelper = userContextHelper;
    }

    protected User getUserFromAuthentication(Authentication auth) {
        return userContextHelper.extractUser(auth);
    }
}
