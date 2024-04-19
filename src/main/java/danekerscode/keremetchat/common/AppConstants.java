package danekerscode.keremetchat.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppConstants {

    USER_NOTIFICATIONS_REDIS_HASH("user_notification"),
    USER_ACTIVITY_REDIS_HASH("user_activity");

    private final String value;
}

