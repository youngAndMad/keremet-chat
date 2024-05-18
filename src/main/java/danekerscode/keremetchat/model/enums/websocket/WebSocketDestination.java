package danekerscode.keremetchat.model.enums.websocket;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebSocketDestination {
    START_LIVE_USER_NOTIFICATION("/user/chat/live/"), // ?
    STOP_LIVE_USER_NOTIFICATION("/user/notification/live/stop/"), //
    USER_NOTIFICATION("/user/notification/");

    @NotNull
    private final String destination;

    public String forChat(Long chatId){
        return getDestination().concat(String.valueOf(chatId));
    }
}
