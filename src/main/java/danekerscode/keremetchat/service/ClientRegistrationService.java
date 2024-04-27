package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.ClientRegistrationRequest;
import danekerscode.keremetchat.model.dto.response.ClientRegistrationResponse;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Collection;
import java.util.Iterator;

public interface ClientRegistrationService {

    void save(ClientRegistrationRequest clientRegistrationRequest);

    void delete(String registrationId);

    Iterator<ClientRegistrationResponse> findAll();

    void update(ClientRegistrationRequest clientRegistrationRequest);

    ClientRegistration findByRegistrationId(String registrationId);

    Collection<ClientRegistration> findAllForAdmin();
}
