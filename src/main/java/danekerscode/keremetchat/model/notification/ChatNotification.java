package danekerscode.keremetchat.model.notification;

import danekerscode.keremetchat.model.entity.BaseEntity;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatNotification extends BaseEntity {
    @Id
    private Long id;
    private Long chatId;
    private Long senderId;
    private WebsocketNotificationType type;
    private Long innerId; // counter of notification amount for each chat
    private LocalDateTime notificationTime = LocalDateTime.now();
}
