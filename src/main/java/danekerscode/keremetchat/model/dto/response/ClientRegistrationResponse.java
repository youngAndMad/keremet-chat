package danekerscode.keremetchat.model.dto.response;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

public record ClientRegistrationResponse(
        CommonOAuth2Provider provider,
        String registrationId
) {
}
