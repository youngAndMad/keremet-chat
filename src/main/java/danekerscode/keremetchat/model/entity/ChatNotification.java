package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatNotification<Content> extends BaseEntity {
    @Id
    private Long id;
    private Long innerId;
    private Long chatId;
    private LocalDateTime notificationTime;
    private WebsocketNotificationType type;
    private Content content;
}
