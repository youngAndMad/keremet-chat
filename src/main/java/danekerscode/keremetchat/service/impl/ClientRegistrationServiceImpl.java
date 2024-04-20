package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.security.JdbcClientRegistrationRepository;
import danekerscode.keremetchat.service.ClientRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientRegistrationServiceImpl implements ClientRegistrationService {

    private final JdbcClientRegistrationRepository clientRegistrationRepository;

    @Override
    public boolean save(ClientRegistration clientRegistration) {
        var alreadyExists = clientRegistrationRepository.persist(clientRegistration);

        log.info(
                "Client registration {} for client {}",
                alreadyExists ? "updated" : "created",
                clientRegistration.getClientName()
        );

        return alreadyExists;
    }

    @Override
    public void delete(String registrationId) {
        clientRegistrationRepository.delete(registrationId);
    }

    @Override
    public Iterator<ClientRegistration> findAll() {
        return clientRegistrationRepository.iterator();
    }

    @Override
    public void update(ClientRegistration clientRegistration) {
        this.save(clientRegistration);//todo
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return clientRegistrationRepository.findByRegistrationId(registrationId);
    }
}
