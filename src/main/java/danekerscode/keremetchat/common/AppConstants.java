package danekerscode.keremetchat.common;

import danekerscode.keremetchat.model.UserActivity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppConstants {
    USER_ACTIVITY_REDIS_SET("user_activity");

    private final String value;

}

