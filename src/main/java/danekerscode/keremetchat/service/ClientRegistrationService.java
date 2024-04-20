package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.ClientRegistrationRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Iterator;

public interface ClientRegistrationService {

    boolean save(ClientRegistrationRequest clientRegistrationRequest);

    void delete(String registrationId);

    Iterator<ClientRegistration> findAll();

    void update(ClientRegistrationRequest clientRegistrationRequest);

    ClientRegistration findByRegistrationId(String registrationId);
}
