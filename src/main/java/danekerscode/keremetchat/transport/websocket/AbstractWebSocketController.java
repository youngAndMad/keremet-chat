package danekerscode.keremetchat.transport.websocket;


import danekerscode.keremetchat.core.helper.UserContextHelper;
import danekerscode.keremetchat.core.helper.WebSocketMessagingHelper;
import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbstractWebSocketController {

    @Autowired
    private UserContextHelper userContextHelper;
    @Autowired
    private WebSocketMessagingHelper webSocketMessagingHelper;
    @Autowired
    private ChatMemberService chatMemberService;
    @Autowired
    private UserStatusService userStatusService;

    protected User getUserFromAuthentication(Authentication auth) {
        return userContextHelper.extractUser(auth);
    }

    protected UserResponseDto getUserResponseDtoFromAuth(Authentication auth) {
        return userContextHelper.asResponseDto(auth);
    }


    protected List<Long> findChatMemberUsersId(Long chatId) {
        return this.chatMemberService.findChatMemberUsersId(chatId);
    }

    protected UserActivity getUserActivity(Long userId) {
        return this.userStatusService.getUserActivity(userId);
    }


}
