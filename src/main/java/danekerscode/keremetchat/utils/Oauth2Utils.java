package danekerscode.keremetchat.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.stream.Stream;

@UtilityClass
public class Oauth2Utils {

    public static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        return Stream.of(
                        AuthorizationGrantType.AUTHORIZATION_CODE,
                        AuthorizationGrantType.CLIENT_CREDENTIALS,
                        AuthorizationGrantType.PASSWORD,
                        AuthorizationGrantType.JWT_BEARER,
                        AuthorizationGrantType.DEVICE_CODE
                )
                .filter(grantType -> grantType.getValue().equals(authorizationGrantType))
                .findFirst()
                .orElseGet(() -> {
                    if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
                        return AuthorizationGrantType.REFRESH_TOKEN;
                    } else {
                        return new AuthorizationGrantType(authorizationGrantType);
                    }
                });
    }


    public static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        return Stream.of(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                        ClientAuthenticationMethod.CLIENT_SECRET_POST,
                        ClientAuthenticationMethod.CLIENT_SECRET_JWT,
                        ClientAuthenticationMethod.PRIVATE_KEY_JWT
                )
                .filter(method -> method.getValue().equals(clientAuthenticationMethod))
                .findFirst()
                .orElseGet(() -> {
                    if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
                        return ClientAuthenticationMethod.NONE;
                    } else {
                        return new ClientAuthenticationMethod(clientAuthenticationMethod);
                    }
                });
    }


    public static AuthenticationMethod resolveUserInfoAuthenticationMethod(
            String userInfoAuthenticationMethod
    ) {
        return Stream.of(
                        AuthenticationMethod.FORM,
                        AuthenticationMethod.HEADER,
                        AuthenticationMethod.QUERY
                )
                .filter(method -> method.getValue().equals(userInfoAuthenticationMethod))
                .findFirst()
                .orElseGet(() -> new AuthenticationMethod(userInfoAuthenticationMethod));
    }
}
