package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.repository.ChatMemberRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMemberServiceImpl implements ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    @Override
    public ChatMember forUserWithRole(User user, ChatUserRole chatUserRole, Chat chat) {
        var chatMember = new ChatMember();
        chatMember.setUser(user);
        chatMember.setRole(chatUserRole);
        chatMember.setChat(chat);
        return chatMemberRepository.save(chatMember);
    }

}
