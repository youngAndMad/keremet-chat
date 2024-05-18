package danekerscode.keremetchat.transport.websocket;


import danekerscode.keremetchat.core.helper.UserContextHelper;
import danekerscode.keremetchat.core.helper.WebSocketMessagingHelper;
import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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

//    protected List<ChatMember> findChatMembers(Long chatId){
//        return this.chatMemberService.findChatMemberUsersId(chatId)
//                .stream().map(m -> chatMemberService.findByChatAndUser(chatId, m))
//                .toList();
//    }

    protected ChatMember findMemberByUserAndChat(Long userId, Long chatId) {
        return chatMemberService.findByChatAndUser(chatId, userId);
    }

    protected UserActivity getUserActivity(Long userId) {
        return this.userStatusService.getUserActivity(userId);
    }

    protected <T extends Serializable> void deliverWebSocketMessage(
            T data,
            WebSocketDestination webSocketDestination,
            Long chatId
    ) {
        webSocketMessagingHelper.deliver(data, webSocketDestination.forChat(chatId));
    }
}
