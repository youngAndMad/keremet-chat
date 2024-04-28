package danekerscode.keremetchat.model.enums.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum WebsocketNotificationType {
    TYPING(true); // todo add more websocket notification types

    private final boolean isRealTime;
}
