package danekerscode.keremetchat.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppConstant {
    USER_NOTIFICATION_REDIS_SET_PREFIX("user_notification:"),

    ENV_COMMON_OAUTH2_PROVIDER_PLACEHOLDER_PATTERN("spring.security.oauth2.client.registration.%s.common-provider-type"),

    REQUEST_START_TIME("request_start_time"),

    LOGGING_PATH_PATTERN("/api/**"),

    DOT("."),
    EMPTY_STRING(""),
    MINIO_DEFAULT_BUCKET("keremet-chat"),

    DEFAULT_ID_COLUMN_NAME("id"),

    DEFAULT_SUCCESS_LOGIN_REDIRECT_URL("/api/v1/auth/me"),

    MANUAL_AUTH_TYPE("MANUAL"),
    AUTHORIZATION_REQUEST_BASE_URL("/oauth2/authorization/"),
    OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME("oauth2_auth_request"),
    REDIRECT_URI_PARAM_COOKIE_NAME("redirect_uri"),

    USER_NOTIFICATIONS_REDIS_HASH("user_notification"),
    USER_ACTIVITY_REDIS_HASH("user_activity");

    private final String value;
}

