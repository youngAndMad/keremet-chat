package danekerscode.keremetchat.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileEntitySource {
    CHAT_AVATAR("chat-avatar"),
    MESSAGE_CONTENT("message-content");

    private final String path;
}
