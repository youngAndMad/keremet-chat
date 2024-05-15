package danekerscode.keremetchat.model.enums.websocket;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebSocketDestination {
    LIVE_USER_NOTIFICATION("/user/notification/live/"), // ?
    USER_NOTIFICATION("/user/notification/");

    @NotNull
    private final String destination;

    public String forUser(Long userId){
        return getDestination().concat(String.valueOf(userId));
    }
}
