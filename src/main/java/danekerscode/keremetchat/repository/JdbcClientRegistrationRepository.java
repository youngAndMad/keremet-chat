package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.dto.response.ClientRegistrationResponse;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.Collection;

public interface JdbcClientRegistrationRepository extends ClientRegistrationRepository,
        Iterable<ClientRegistrationResponse> {

    void deleteByRegistrationId(String registrationId);

    void save(ClientRegistration clientRegistration, CommonOAuth2Provider provider);

    Collection<ClientRegistration> findAll();

    CommonOAuth2Provider getProviderNameForClientRegistration(String registrationId);
}
