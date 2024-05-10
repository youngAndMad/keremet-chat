package danekerscode.keremetchat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class UserNotificationState {
    @Id
    private Long id;
    private Long userId;
    private Long chatId;
    private Long lastReceivedNotificationId;
}
