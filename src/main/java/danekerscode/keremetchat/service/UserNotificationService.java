package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.UserNotification;

import java.io.Serializable;
import java.util.List;

public interface UserNotificationService {

    <Content extends Serializable> UserNotification<Content> save(
            UserNotification<Content> userNotification
    );

    List<UserNotification> getUserNotifications(Long userId);

}
