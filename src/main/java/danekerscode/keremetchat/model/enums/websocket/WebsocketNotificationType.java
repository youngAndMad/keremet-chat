package danekerscode.keremetchat.model.enums.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum WebsocketNotificationType {
    TYPING(true, List.of(
            WebSocketDestinationType.DestinationType.CHAT_BASED,
            WebSocketDestinationType.DestinationType.PRIVATE
    )); // todo add more websocket notification types

    private final boolean isRealTime;
    private final List<WebSocketDestinationType.DestinationType> destinationTypes;

    public boolean isGlobalNotification(){
        return getDestinationTypes().contains(WebSocketDestinationType.DestinationType.GLOBAL);
    }

}
