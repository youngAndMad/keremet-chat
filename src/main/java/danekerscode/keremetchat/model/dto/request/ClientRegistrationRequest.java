package danekerscode.keremetchat.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

public record ClientRegistrationRequest(
        @NotNull
        CommonOAuth2Provider provider,
        @NotNull @NotEmpty
        String clientId,
        @NotNull @NotEmpty
        String clientSecret,
        @NotNull @NotEmpty
        String registrationId
){ }
