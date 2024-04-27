package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.enums.ChatType;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import danekerscode.keremetchat.repository.ChatRepository;
import danekerscode.keremetchat.service.ChatMemberService;
import danekerscode.keremetchat.service.ChatService;
import danekerscode.keremetchat.service.FileStorageService;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberService chatMemberService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public IdDto<Long> createChat(CreatePrivateChatRequest createChatRequest) {
        var chat = chatRepository.save(new Chat());

        var chatOwner = chatMemberService.forUserWithRole(UserContextHolder.getContext(), ChatUserRole.DEFAULT, chat);
        var secondChatMember = userService.findByEmail(createChatRequest.receiverEmail());

        var chatMember = chatMemberService.forUserWithRole(secondChatMember, ChatUserRole.DEFAULT, chat);

        chat.setName(chatOwner.getUser().getUsername() + ":" + chatMember.getUser().getUsername());
        chat.setType(ChatType.PRIVATE);
//        chat.setMembers(List.of(chatOwner, chatMember));
        chatRepository.save(chat);

        return new IdDto<>(chat.getId());
    }

    @Override
    public void deletePrivateChat(Long chatId) {
        var currentUser = UserContextHolder.getContext();

        if (!chatRepository.isExistByID(chatId)) {
            throw new EntityNotFoundException(Chat.class, chatId);
        }

        var chat = chatRepository.findByID(chatId);

        if(chat.getType() != ChatType.PRIVATE){
            throw new InvalidRequestPayloadException("Chat is not private");
        }

        if(chat.getMembers().stream().noneMatch(chatMember -> chatMember.getUser().getId().equals(currentUser.getId()))){
            throw new InvalidRequestPayloadException("User is not a member of the chat");
        }

        chatRepository.deleteById(chatId);
    }

    @Override
    @Transactional
    public List<Long> getMemberUsersIdList(Long chatId) {
        return chatRepository.getMemberUsersIdList(chatId);
    }

    @Override
    public boolean existsById(Long chatId) {
        return chatRepository.isExistByID(chatId);
    }

    @Override
    @Transactional
    public void uploadAvatar(MultipartFile file, Long chatId) {
        this.chatRepository.checkExists(chatId);

        var fileEntity = fileStorageService.save(file, FileEntitySource.CHAT_AVATAR, String.valueOf(chatId));

        this.chatRepository.addAvatar(chatId,fileEntity.getId());
    }

    @Override
    @Transactional
    public void deleteAvatar(Long fileId, Long chatId) {
        this.chatRepository.checkExists(chatId);

        this.chatRepository.deleteAvatar(chatId,fileId);

        this.fileStorageService.deleteFile(fileId);
    }
}
