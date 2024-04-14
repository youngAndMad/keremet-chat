package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;

public interface ChatMemberService {

    ChatMember forUserWithRole(User user, ChatUserRole chatUserRole, Chat chat);

}