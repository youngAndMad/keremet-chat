package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.UserActivity;

public interface UserStatusService {

    UserActivity getUserActivity(Long userId);

    void setOnlineStatus(Long userId);

    void setOfflineStatus(Long userId);
}
