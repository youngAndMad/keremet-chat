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
    private String id = UUID.randomUUID().toString();
    private Long userId;
    private LocalDateTime createdTime = LocalDateTime.now();
    private WebsocketNotificationType type;
    @Nullable
    private Content content;

    public UserNotification(Long userId,
                            WebsocketNotificationType type,
                            @Nullable Content content) {
        this.userId = userId;
        this.type = type;
        this.content = content;
    }
}
