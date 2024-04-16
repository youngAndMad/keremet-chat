package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.common.annotation.FetchUserContext;
import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/private")
    @FetchUserContext
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a private chat", responses = {
            @ApiResponse(responseCode = "201", description = "Chat created")
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
    void deletePrivateChat(
            @PathVariable Long chatId
    ) {
        chatService.deletePrivateChat(chatId);
    }
}
