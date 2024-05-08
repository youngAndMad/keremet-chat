package danekerscode.keremetchat.model.enums.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum WebsocketNotificationType {
    MESSAGE(false),
    SENDING_VIDEO(true),
    SENDING_PHOTO(true),
    RECORDING_VOICE_MESSAGE(true),
    RECORDING_VIDEO_MESSAGE(true),
    JOIN_CHAT(false),
    LEFT_CHAT(false),
    PINNED_MESSAGE (false),
    TYPING(true);

    private final boolean onlyForOnlineUsers;
}
