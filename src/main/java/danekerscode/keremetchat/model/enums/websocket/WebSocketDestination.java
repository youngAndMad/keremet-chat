package danekerscode.keremetchat.model.enums.websocket;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebSocketDestination {
    USER_NOTIFICATION("/user/notification/");
    @NotNull
    private final String destination;
}
