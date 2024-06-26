package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.core.annotation.FetchUserContext;
import danekerscode.keremetchat.model.dto.ChatSettingsDto;
import danekerscode.keremetchat.service.ChatSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/settings")
@Tag(name = "Chat settings")
@RestController
public class ChatSettingsController {

    private final ChatSettingsService chatSettingsService;

    @PutMapping("{chatId}")
    @FetchUserContext
    @Operation(description = "Update chat settings by chatId")
    void updateChatSettings(
            @RequestBody @Validated ChatSettingsDto chatSettingsDto,
            @PathVariable Long chatId
    ) {
        chatSettingsService.updateSettingsFor(chatId, chatSettingsDto, UserContextHolder.get());
    }

    @Operation(description = "Reset chat settings to default")
    @FetchUserContext
    @PutMapping("default/{chatId}")
    void resetToDefault(@PathVariable Long chatId) {
        chatSettingsService.setDefaultForChat(chatId, UserContextHolder.get());
    }


}
