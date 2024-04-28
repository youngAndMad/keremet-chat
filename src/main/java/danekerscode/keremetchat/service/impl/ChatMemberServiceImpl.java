package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.ChatMemberRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatMemberServiceImpl implements ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatService chatService;

    public ChatMemberServiceImpl(ChatMemberRepository chatMemberRepository, @Lazy ChatService chatService) {
        this.chatMemberRepository = chatMemberRepository;
        this.chatService = chatService;
    }

    @Override
    public ChatMember forUserWithRole(User user, ChatUserRole chatUserRole, Chat chat) {
        var chatMember = new ChatMember();
        chatMember.setUser(user);
        chatMember.setRole(chatUserRole);
        chatMember.setChat(chat);
        return chatMemberRepository.save(chatMember);
    }

    @Override
    public Optional<Long> findByChatIdAndUserId(Long chatId, Long userId) {
        return Optional.ofNullable(chatMemberRepository.findByChatIdAndUserId(chatId, userId));
    }

    @Override
    public List<Long> findChatMemberUsersId(Long chatId) {
        var chatExists = chatService.existsById(chatId);

        if (!chatExists) {
            throw new EntityNotFoundException(Chat.class, chatId);
        }

        return chatMemberRepository.findChatMemberUsersId(chatId);
    }

}
