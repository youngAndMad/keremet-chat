package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.CreateGroupChatRequest;
import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.projection.ChatProjection;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {

    IdDto<Long> createPrivateChat(CreatePrivateChatRequest createChatRequest,User currentUser);

    IdDto<Long> createGroupChat(CreateGroupChatRequest createGroupChatRequest, User currentUser);

    void deleteChat(Long chatId, User currentUser);

    boolean existsById(Long chatId);

    void uploadAvatar(MultipartFile file, Long chatId);

    void deleteAvatar(Long fileId, Long chatId);

    ChatProjection findById(Long chatId);

    void processUserInvitation(Long userId, Long chatId, User currentUser);

    void deleteChatMember(Long userId, Long chatId, User currentUser);
}
