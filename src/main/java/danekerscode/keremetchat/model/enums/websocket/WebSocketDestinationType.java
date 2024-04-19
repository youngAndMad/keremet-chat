package danekerscode.keremetchat.model.enums.websocket;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebSocketDestinationType {
    NOTIFICATION_NOTIFICATION("/user/notification/{userId}", DestinationType.PRIVATE, "{userid}");


    private final String destination;
    private final DestinationType destinationType;
    @Nullable
    private final String identifier;

    @RequiredArgsConstructor
    @Getter
    public enum DestinationType {
        PRIVATE(true),
        CHAT_BASED(true),
        GLOBAL(false);

        private final boolean withIdentifier;
    }

}
