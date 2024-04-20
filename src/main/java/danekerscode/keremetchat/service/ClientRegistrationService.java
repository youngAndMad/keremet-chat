package danekerscode.keremetchat.service;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Iterator;

public interface ClientRegistrationService {

    boolean save(ClientRegistration clientRegistration);

    void delete(String registrationId);

    Iterator<ClientRegistration> findAll();

    void update(ClientRegistration clientRegistration);

    ClientRegistration findByRegistrationId(String registrationId);
}
