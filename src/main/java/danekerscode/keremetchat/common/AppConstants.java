package danekerscode.keremetchat.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppConstants {

    MINIO_DEFAULT_BUCKET("keremetchat"),

    DEFAULT_SUCCESS_LOGIN_REDIRECT_URL("/api/v1/auth/me"),

    MANUAL_AUTH_TYPE("MANUAL"),
    OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME("oauth2_auth_request"),
    REDIRECT_URI_PARAM_COOKIE_NAME("redirect_uri"),

    USER_NOTIFICATIONS_REDIS_HASH("user_notification"),
    USER_ACTIVITY_REDIS_HASH("user_activity");

    private final String value;
}

