package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.service.ChatService;
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
    @ResponseStatus(HttpStatus.CREATED)
    IdDto<Long> createChat(
            @RequestBody @Validated CreatePrivateChatRequest createChatRequest
    ) {
        return chatService.createChat(createChatRequest);
    }

}
