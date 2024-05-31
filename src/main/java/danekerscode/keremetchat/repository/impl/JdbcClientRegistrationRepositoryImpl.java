package danekerscode.keremetchat.repository.impl;

import danekerscode.keremetchat.model.dto.KeyPair;
import danekerscode.keremetchat.model.dto.response.ClientRegistrationResponse;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.JdbcClientRegistrationRepository;
import danekerscode.keremetchat.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class JdbcClientRegistrationRepositoryImpl implements JdbcClientRegistrationRepository {

    private final static RowMapper<ClientRegistration> CLIENT_REGISTRATION_MAPPER =
            (rs, _rowNum) -> JdbcUtils.mapToClientRegistration(rs);
    private final JdbcClient jdbcClient;

    public JdbcClientRegistrationRepositoryImpl(
            JdbcClient jdbcClient,
            Map<CommonOAuth2Provider, List<ClientRegistration>> clientRegistrationMap
    ) {
        Assert.notNull(jdbcClient, "JdbcClient can not be null");
        this.jdbcClient = jdbcClient;

        clientRegistrationMap.forEach(((provider, clientRegistrations) -> {
            clientRegistrations.forEach(clientRegistration -> {
                this.deleteByRegistrationId(clientRegistration.getRegistrationId());
                this.save(clientRegistration, provider);
            });
        }));
    }

    @NotNull
    @Override
    public Iterator<ClientRegistrationResponse> iterator() {
        return jdbcClient.sql("select * from oauth2_client_registration")
                .query(CLIENT_REGISTRATION_MAPPER)
                .stream()
                .map(c -> new ClientRegistrationResponse(c.getRegistrationId(), this.getProviderNameForClientRegistration(c.getRegistrationId())))
                .iterator();
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        Assert.hasText(registrationId, "RegistrationId cannot be empty");
        return jdbcClient.sql("select * from oauth2_client_registration where registration_id = :registrationId")
                .param("registrationId", registrationId)
                .query(CLIENT_REGISTRATION_MAPPER)
                .optional()
                .orElseThrow(() -> new EntityNotFoundException(ClientRegistration.class, registrationId));
    }

    public void save(ClientRegistration clientRegistration, CommonOAuth2Provider provider) {
        JdbcUtils.enrichJdbcStatementForClientRegistration(
                jdbcClient.sql("""
                        insert into oauth2_client_registration(
                        registration_id,client_id,client_secret,client_authentication_method,authorization_grant_type,
                        client_name,redirect_uri,scopes,authorization_uri,token_uri,jwk_set_uri,issuer_uri,user_info_uri,
                        user_info_authentication_method,user_name_attribute_name,configuration_metadata, provider_name
                        ) values (
                        :registrationId,:clientId,:clientSecret,:clientAuthenticationMethod,:authorizationGrantType,
                        :clientName,:redirectUri,:scopes,:authorizationUri,:tokenUri,:jwkSetUri,:issuerUri,:userInfoUri,
                        :userInfoAuthenticationMethod,:userNameAttributeName,:configurationMetadata,:providerName
                        );
                        """),
                clientRegistration,
                provider
        ).update();
    }

    @Override
    public Collection<ClientRegistration> findAll() {
        return jdbcClient.sql("select * from oauth2_client_registration")
                .query(CLIENT_REGISTRATION_MAPPER)
                .list();
    }

    public void deleteByRegistrationId(String registrationId) {
        var affectedRows = jdbcClient.sql("""
                        delete from oauth2_client_registration
                        where registration_id = :registrationId
                        """)
                .param("registrationId", registrationId)
                .update();
    }

    @Override
    public CommonOAuth2Provider getProviderNameForClientRegistration(String registrationId) {
        return jdbcClient.sql("""
                        select provider_name from oauth2_client_registration
                        where registration_id = :registrationId
                        """)
                .param("registrationId", registrationId)
                .query(CommonOAuth2Provider.class)
                .optional()
                .orElseThrow(() -> new EntityNotFoundException(ClientRegistration.class, KeyPair.of("registrationId", registrationId)));
    }


}
