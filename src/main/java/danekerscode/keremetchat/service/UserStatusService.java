package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.UserActivity;

/**
 * This interface defines methods for managing user status and activity.
 */
public interface UserStatusService {

    /**
     * Retrieves the current activity of the specified user.
     *
     * @param userId The unique identifier of the user.
     * @return UserActivity object representing the user's current activity.
     */
    UserActivity getUserActivity(Long userId);

    /**
     * Sets the online status for the specified user.
     *
     * @param userId The unique identifier of the user.
     */
    void setOnlineStatus(Long userId);

    /**
     * Sets the offline status for the specified user.
     *
     * @param userId The unique identifier of the user.
     */
    void setOfflineStatus(Long userId);
}
