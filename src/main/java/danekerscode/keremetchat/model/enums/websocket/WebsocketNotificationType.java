package danekerscode.keremetchat.model.enums.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum WebsocketNotificationType {
    TYPING(true, List.of(
            WebSocketDestination.DestinationType.CHAT_BASED,
            WebSocketDestination.DestinationType.PRIVATE
    )); // todo add more websocket notification types

    private final boolean isRealTime;
    private final List<WebSocketDestination.DestinationType> destinationTypes;

    public boolean isGlobalNotification(){
        return getDestinationTypes().contains(WebSocketDestination.DestinationType.GLOBAL);
    }

}
