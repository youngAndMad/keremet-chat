package danekerscode.keremetchat.repository;


import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;

import java.time.LocalDateTime;

public interface ChatNotificationRepository {

    <Content> IdDto<Long> save(Long chatId, LocalDateTime notificationTime, WebsocketNotificationType type, Content content);

    void delete(Long id);

}
