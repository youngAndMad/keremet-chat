package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.core.annotation.FetchUserContext;
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
    @Operation(description = "Create a private chat", responses = {
            @ApiResponse(responseCode = "201", description = "Chat created. Will return id of created chat")
    })
    IdDto<Long> createChat(
            @RequestBody @Validated CreatePrivateChatRequest createChatRequest
    ) {
        return chatService.createChat(createChatRequest);
    }

    @DeleteMapping("/private/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete a private chat", responses = {
            @ApiResponse(responseCode = "204", description = "Chat deleted")
    })
    @FetchUserContext
    void deletePrivateChat(
            @PathVariable Long chatId
    ) {
        chatService.deletePrivateChat(chatId);
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
    void uploadAvatar(
            @PathVariable Long id,
            @PathVariable Long fileId
    ) {
        this.chatService.deleteAvatar(fileId, id);
    }

}
