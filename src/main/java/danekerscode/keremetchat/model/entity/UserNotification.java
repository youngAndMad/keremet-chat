package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserNotification {
    private String id;

    private LocalDateTime receivedTime;
    private Long userId;
    private WebsocketNotificationType type;
}