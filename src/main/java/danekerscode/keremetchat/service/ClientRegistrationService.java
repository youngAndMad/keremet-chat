package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.ClientRegistrationRequest;
import danekerscode.keremetchat.model.dto.response.ClientRegistrationResponse;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ClientRegistrationService {

    void save(ClientRegistrationRequest clientRegistrationRequest);

    void delete(String registrationId);

    Iterator<ClientRegistrationResponse> findAll();

    void update(ClientRegistrationRequest clientRegistrationRequest);

    ClientRegistration findByRegistrationId(String registrationId);

    Map<CommonOAuth2Provider, List<ClientRegistration>> findAllForAdmin();
}
