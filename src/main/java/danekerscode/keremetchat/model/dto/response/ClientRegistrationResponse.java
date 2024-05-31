package danekerscode.keremetchat.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

@AllArgsConstructor
@Data
public class ClientRegistrationResponse {
    private String registrationId;
    private CommonOAuth2Provider provider;
}
