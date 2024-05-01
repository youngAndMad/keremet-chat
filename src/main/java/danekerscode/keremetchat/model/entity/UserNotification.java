package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class UserNotification<Content extends Serializable> {
    private String id;
    private Long userId;
    private LocalDateTime createdTime = LocalDateTime.now();
    private WebsocketNotificationType type;
    @Nullable
    private Content content;

    public UserNotification(Long userId,
                            LocalDateTime createdTime,
                            WebsocketNotificationType type,
                            @Nullable Content content) {
        this.userId = userId;
        this.createdTime = createdTime;
        this.type = type;
        this.content = content;
        this.id = UUID.randomUUID().toString();
    }

    public UserNotification() {
        this.id = UUID.randomUUID().toString();
    }
}
