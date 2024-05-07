package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.AppConstants;
import danekerscode.keremetchat.model.dto.request.websocket.DeliverNotificationRequest;
import danekerscode.keremetchat.model.entity.UserNotification;
import danekerscode.keremetchat.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {

    private final SetOperations<String, Object> setOperations;

    @Override
    public <Content extends Serializable> UserNotification<Content> save(
            UserNotification<Content> userNotification
    ) {
        var setKay = getUserNotificationsSetKey(userNotification.getUserId());

        setOperations.add(setKay, userNotification);

        return userNotification;
    }

    @Override
    public List<UserNotification> getUserNotifications(Long userId) {
        var setKay = getUserNotificationsSetKey(userId);

        return Optional.ofNullable(setOperations.members(setKay))
                .stream()
                .flatMap(Collection::stream)
                .map(UserNotification.class::cast)
                .toList();
    }

    private String getUserNotificationsSetKey(Long userId) {
        return AppConstants.USER_NOTIFICATION_REDIS_SET_PREFIX.getValue()
                .concat(String.valueOf(userId));
    }
}
