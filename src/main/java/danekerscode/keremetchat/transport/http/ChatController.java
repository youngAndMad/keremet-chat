package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.core.annotation.FetchUserContext;
import danekerscode.keremetchat.model.dto.request.CreateGroupChatRequest;
import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Chat")
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/private")
    @FetchUserContext
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a private chat")
    IdDto<Long> createPrivateChat(
            @RequestBody @Validated CreatePrivateChatRequest createChatRequest
    ) {
        return chatService.createPrivateChat(createChatRequest, UserContextHolder.getContext());
    }

    @PostMapping
    @Operation(description = "Create a group chat")
    @ResponseStatus(HttpStatus.CREATED)
    @FetchUserContext
    IdDto<Long> createChat(
            @RequestBody @Validated CreateGroupChatRequest createGroupChatRequest
    ) {
        return chatService.createGroupChat(createGroupChatRequest, UserContextHolder.getContext());
    }

    @DeleteMapping("{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete a private chat", responses = {
            @ApiResponse(responseCode = "204", description = "Chat deleted")
    })
    @FetchUserContext
    void deleteChat(
            @PathVariable Long chatId
    ) {
        chatService.deleteChat(chatId, UserContextHolder.getContext());
    }

    @PostMapping("{id}/avatar")
    @Operation(description = "Upload avatar for chat")
    void uploadAvatar(
            @PathVariable Long id,
            @RequestParam MultipartFile file
    ) {
        this.chatService.uploadAvatar(file, id);
    }

    @DeleteMapping("{id}/avatar/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete avatar of chat by file id and chat id")
    void deleteAvatar(
            @PathVariable Long id,
            @PathVariable Long fileId
    ) {
        this.chatService.deleteAvatar(fileId, id);
    }

    @PostMapping("{id}/invite/{userId}")
    @Operation(description = "Invite member to chat")
    @FetchUserContext
    void inviteUser(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        chatService.processUserInvitation(userId, id, UserContextHolder.getContext());
    }

    @FetchUserContext
    @Operation(description = "Delete member f")
    @PostMapping("{id}/block/{userId}")
    void deleteUserFromChat(
            @PathVariable Long id,
            @PathVariable Long userId
    ){
        chatService.deleteChatMember(userId, id, UserContextHolder.getContext());
    }
}
