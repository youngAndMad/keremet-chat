package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;

import java.util.List;

public interface ChatMemberService {

    ChatMember forUserWithRole(User user, ChatUserRole chatUserRole, Chat chat);

    List<Long> findChatMemberUsersId(Long chatId);

    ChatMember findByChatAndUser(Long chatId, Long userId);

    void updateLastNotificationState(Long chatMemberId, Long updatedValue);
}
