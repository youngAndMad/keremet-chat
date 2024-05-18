package danekerscode.keremetchat.model.enums;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

import java.util.Arrays;

public enum AuthType{
    MANUAL,
    GITHUB,
    GOOGLE,
    OKTA,
    FACEBOOK;

    public static AuthType fromCommonOauth2Provider(CommonOAuth2Provider provider){
        return Arrays.stream(AuthType.values())
                .filter(authType -> authType.name().equalsIgnoreCase(provider.name()))
                .findAny()
                .orElseGet(() -> AuthType.MANUAL);
    }
}
