package danekerscode.keremetchat.model.enums.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WebsocketNotificationType {
    MESSAGE(false),
    SENDING_VIDEO(true),
    SENDING_PHOTO(true),
    RECORDING_VOICE_MESSAGE(true),
    RECORDING_VIDEO_MESSAGE(true),
    JOIN_CHAT(false),
    LEFT_CHAT(false),
    PINNED_MESSAGE(false),
    TYPING(true);

    private final boolean onlyForOnlineUsers;
    private final Class<?> notificationPayloadClass;

    WebsocketNotificationType(boolean onlyForOnlineUsers) {
        this(onlyForOnlineUsers, null); //todo refactor delete this constructor
    }
}
