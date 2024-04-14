package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.ChatType;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.repository.ChatRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatService;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberService chatMemberService;
    private final UserService userService;

    @Override
    @Transactional
    public IdDto<Long> createChat(CreatePrivateChatRequest createChatRequest) {
        var chat = chatRepository.save(new Chat());

        var chatOwner = chatMemberService.forUserWithRole(UserContextHolder.getContext(), ChatUserRole.DEFAULT, chat);
        var secondChatMember = userService.findByEmail(createChatRequest.receiverEmail());

        var chatMember = chatMemberService.forUserWithRole(secondChatMember, ChatUserRole.DEFAULT, chat);

        chat.setName(chatOwner.getUser().getUsername() + ":" + chatMember.getUser().getUsername());
        chat.setType(ChatType.PRIVATE);
        chat.setMembers(List.of(chatOwner, chatMember));
        chatRepository.save(chat);

        return new IdDto<>(chat.getId());
    }
}