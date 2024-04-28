package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;

import java.util.List;
import java.util.Optional;

public interface ChatMemberService {

    ChatMember forUserWithRole(User user, ChatUserRole chatUserRole, Chat chat);

    Optional<Long> findByChatIdAndUserId(Long chatId, Long userId);

    List<Long> findChatMemberUsersId(Long chatId);
}
