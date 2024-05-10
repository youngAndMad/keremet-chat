package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.UserNotificationState;

import java.util.List;

public interface UserNotificationStateRepository {

    List<UserNotificationState> findAllByUserId(Long id);

    IdDto<Long> save(Long chatId, Long userId);

    void update(Long chatId, Long userId, Long newLastReceivedNotificationId);
}
