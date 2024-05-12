package danekerscode.keremetchat.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class UserNotificationState {
    @Id
    private Long id;
    private Long userId;
    private Long chatId;
    private Long lastReceivedNotificationId;
}
