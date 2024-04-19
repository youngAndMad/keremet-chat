package danekerscode.keremetchat.model.enums.websocket;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebSocketDestination {
    USER_NOTIFICATION("/user/notification/{userId}", DestinationType.PRIVATE, "{userid}");
    @NotNull
    private final String destination;
    @NotNull
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
