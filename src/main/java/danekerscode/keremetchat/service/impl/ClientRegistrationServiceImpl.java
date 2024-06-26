package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.request.ClientRegistrationRequest;
import danekerscode.keremetchat.model.dto.response.ClientRegistrationResponse;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.JdbcClientRegistrationRepository;
import danekerscode.keremetchat.service.ClientRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientRegistrationServiceImpl implements ClientRegistrationService {

    private final JdbcClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void save(ClientRegistrationRequest clientRegistrationRequest) {
        var clientRegistration = clientRegistrationRequest.provider()
                .getBuilder(clientRegistrationRequest.registrationId())
                .clientId(clientRegistrationRequest.clientId())
                .clientSecret(clientRegistrationRequest.clientSecret())
                .build();

        clientRegistrationRepository.save(clientRegistration, clientRegistrationRequest.provider());
    }

    @Override
    public void delete(String registrationId) {
        clientRegistrationRepository.deleteByRegistrationId(registrationId);
    }

    @Override
    public Iterator<ClientRegistrationResponse> findAll() {
        return clientRegistrationRepository.iterator();
    }

    @Override
    public void update(ClientRegistrationRequest clientRegistrationRequest) {
        var clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationRequest.registrationId());

        if (clientRegistration == null) {
            throw new EntityNotFoundException(ClientRegistration.class, clientRegistrationRequest.registrationId());
        }

        clientRegistrationRepository.deleteByRegistrationId(clientRegistration.getRegistrationId());
        clientRegistrationRepository.save(clientRegistration, clientRegistrationRequest.provider());
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return clientRegistrationRepository.findByRegistrationId(registrationId);
    }

    @Override
    public Map<CommonOAuth2Provider, List<ClientRegistration>> findAllForAdmin() {
        return this.clientRegistrationRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(cr -> this.clientRegistrationRepository.getProviderNameForClientRegistration(cr.getRegistrationId())));
    }
}
