package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.UserNotificationState;

import java.util.List;

public interface UserNotificationStateService {

    List<UserNotificationState> findUserNotificationsState(Long userId);

    IdDto<Long> save(Long chatId, Long userId);

    void update(Long chatId,Long userId,Long newLastReceivedNotificationId);
}
