package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.UserNotificationState;
import danekerscode.keremetchat.repository.UserNotificationStateRepository;
import danekerscode.keremetchat.service.UserNotificationStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationStateServiceImpl implements UserNotificationStateService {

    private final UserNotificationStateRepository userNotificationStateRepository;

    @Override
    @Transactional
    public List<UserNotificationState> findUserNotificationsState(Long userId) {
        return userNotificationStateRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public IdDto<Long> save(Long chatId, Long userId) {
        return userNotificationStateRepository.save(chatId, userId);
    }

    @Override
    @Transactional
    public void update(Long chatId, Long userId, Long newLastReceivedNotificationId) {
        userNotificationStateRepository.update(chatId, userId, newLastReceivedNotificationId);
    }
}
