package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.request.CreateGroupChatRequest;
import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.*;
import danekerscode.keremetchat.model.enums.ChatType;
import danekerscode.keremetchat.model.enums.ChatUserRole;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import danekerscode.keremetchat.model.projection.ChatProjection;
import danekerscode.keremetchat.repository.ChatRepository;
import danekerscode.keremetchat.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberService chatMemberService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ChatNotificationService chatNotificationService;


    @Override
    @Transactional
    public IdDto<Long> createPrivateChat(
            CreatePrivateChatRequest createChatRequest, User currentUser
    ) {
        var chat = chatRepository.save(new Chat());

        var chatOwner = chatMemberService.forUserWithRole(currentUser, ChatUserRole.OWNER, chat);
        var secondChatMember = userService.findByEmail(createChatRequest.receiverEmail());

        var chatMember = chatMemberService.forUserWithRole(secondChatMember, ChatUserRole.DEFAULT, chat);

        chat.setName(chatOwner.getUser().getUsername() + ":" + chatMember.getUser().getUsername());
        chat.setType(ChatType.PRIVATE);
        chatRepository.save(chat);

        return new IdDto<>(chat.getId());
    }

    @Override
    @Transactional
    public IdDto<Long> createGroupChat(
            CreateGroupChatRequest createGroupChatRequest,
            User currentUser
    ) {
        var chat = chatRepository.save(new Chat());

        var chatOwner = chatMemberService.forUserWithRole(currentUser, ChatUserRole.OWNER, chat);
        var chatMembers = new ArrayList<>(
                createGroupChatRequest.inviteMembers().stream().map(userService::findById)
                        .map(user -> chatMemberService.forUserWithRole(user, ChatUserRole.MEMBER, chat))
                        .toList()
        );

        chatMembers.add(chatOwner);
        chat.setName(createGroupChatRequest.name());
        chat.setMembers(chatMembers);
        chat.setSettings(ChatSettings.defaultSettingsForChat(chat));

        if (createGroupChatRequest.avatar() != null) {
            var fileEntity = fileStorageService.save(
                    createGroupChatRequest.avatar(),
                    FileEntitySource.CHAT_AVATAR,
                    String.valueOf(chat.getId())
            );

            var chatAvatars = new ArrayList<FileEntity>();
            chatAvatars.add(fileEntity);
            chat.setAvatars(chatAvatars);
        }

        chatRepository.save(chat);

        return new IdDto<>(chat.getId());
    }

    @Transactional
    @Override
    public void deleteChat(Long chatId, User currentUser) {
        var chat = this.findById(chatId);

        var currentChatMember = chat.memberForUser(currentUser.getId());

        if (!currentChatMember.isAdmin()) {
            throw new InvalidRequestPayloadException("You are not a owner of current chat");
        }

        chatNotificationService.cascadeForChat(chatId);
        chatRepository.deleteById(chatId);
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

        this.chatRepository.addAvatar(chatId, fileEntity.getId());
    }

    @Override
    @Transactional
    public void deleteAvatar(Long fileId, Long chatId) {
        this.chatRepository.checkExists(chatId);

        this.chatRepository.deleteAvatar(chatId, fileId);

        this.fileStorageService.deleteFile(fileId);
    }

    @Override
    public ChatProjection findById(Long chatId) {
        return chatRepository.findChatById(chatId)
                .orElseThrow(() -> new EntityNotFoundException(Chat.class, chatId));
    }

    @Override
    public void processUserInvitation(Long userId, Long chatId, User currentUser) {
        var chat = fetchChatEntity(chatId);

        if (chat.getType() == ChatType.PRIVATE) {
            throw new InvalidRequestPayloadException("Can not invite users to private chat");
        }

        var userToInvite = userService.findById(userId);
        var chatSettings = chat.getSettings();

        if (!chatSettings.getEveryoneCanInviteMembers() &&
                chat.memberForUser(currentUser.getId()).isNotStaffMember()
        ) {
            throw new InvalidRequestPayloadException("You are not allowed to invite users to this chat. Permission denied");
        }

        var chatMember = chatMemberService.forUserWithRole(userToInvite, ChatUserRole.MEMBER, chat);
        chat.getMembers().add(chatMember);
        chatRepository.save(chat);
    }

    private Chat fetchChatEntity(Long chatId) {
        return this.chatRepository.findByID(chatId);
    }

    @Override
    public void deleteChatMember(Long userId, Long chatId, User currentUser) {
        var chat = fetchChatEntity(chatId);

        var currentChatMember = chat.memberForUser(currentUser.getId());

        if (currentChatMember.isNotStaffMember()){
            throw new InvalidRequestPayloadException("You are not allowed to delete users from this chat. Permission denied");
        }

        chat.getMembers().remove(currentChatMember);
        chatRepository.save(chat);
    }
}
