package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.KeyPair;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.ChatMemberRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatNotificationService;
import danekerscode.keremetchat.service.ChatService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatMemberServiceImpl implements ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatService chatService;
    private final ChatNotificationService chatNotificationService;

    public ChatMemberServiceImpl(ChatMemberRepository chatMemberRepository,
                                 @Lazy ChatService chatService,
                                 ChatNotificationService chatNotificationService) {
        this.chatMemberRepository = chatMemberRepository;
        this.chatService = chatService;
        this.chatNotificationService = chatNotificationService;
    }

    @Override
    public ChatMember forUserWithRole(User user, ChatUserRole chatUserRole, Chat chat) {
        var chatMember = new ChatMember();
        chatMember.setUser(user);
        chatMember.setRole(chatUserRole);
        chatMember.setChat(chat);
        chatMember.setLastReceivedNotificationId(chatNotificationService.lastNotificationInnerId(chat.getId()));
        return chatMemberRepository.save(chatMember);
    }

    @Override
    public List<Long> findChatMemberUsersId(Long chatId) {
        var chatExists = chatService.existsById(chatId);

        if (!chatExists) {
            throw new EntityNotFoundException(Chat.class, chatId);
        }

        return chatMemberRepository.findChatMemberUsersId(chatId);
    }

    @Override
    public ChatMember findByChatAndUser(Long chatId, Long userId) {
        return chatMemberRepository.findByUserIdAndChatId(chatId, userId)
                .orElseThrow(() -> new EntityNotFoundException(ChatMember.class, KeyPair.of("chatId", chatId), KeyPair.of("userId", userId)));
    }

    @Override
    @Transactional
    public void updateLastNotificationState(Long chatMemberId, Long updatedValue) {
        chatMemberRepository.updateLastNotificationState(chatMemberId, updatedValue);
    }

}
