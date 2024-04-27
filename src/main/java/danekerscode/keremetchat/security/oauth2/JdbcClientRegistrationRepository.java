package danekerscode.keremetchat.security.oauth2;

import danekerscode.keremetchat.model.dto.response.ClientRegistrationResponse;
import danekerscode.keremetchat.utils.ClientRegistrationParametersMapper;
import danekerscode.keremetchat.utils.ClientRegistrationRowMapper;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;

@Slf4j
public class JdbcClientRegistrationRepository implements ClientRegistrationRepository,
        Iterable<ClientRegistrationResponse> {

    private static final String COLUMN_NAMES =
            """
                    registration_id,client_id,client_secret,client_authentication_method,authorization_grant_type,
                    client_name,redirect_uri,scopes,authorization_uri,token_uri,jwk_set_uri,issuer_uri,user_info_uri,
                    user_info_authentication_method,user_name_attribute_name,configuration_metadata, provider_name
                    """;
    private static final String TABLE_NAME = "oauth2_client_registration";
    private static final String LOAD_CLIENT_REGISTERED_SQL = "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME;
    private static final String LOAD_CLIENT_REGISTERED_QUERY_SQL = LOAD_CLIENT_REGISTERED_SQL + " WHERE ";
    private static final String INSERT_CLIENT_REGISTERED_SQL = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_CLIENT_REGISTERED_SQL = "UPDATE " + TABLE_NAME + """
             SET client_id = ?,client_secret = ?,client_authentication_method = ?,authorization_grant_type = ?,
             client_name = ?,redirect_uri = ?,scopes = ?,authorization_uri = ?,token_uri = ?,jwk_set_uri = ?,
             issuer_uri = ?,user_info_uri = ?,user_info_authentication_method = ?,user_name_attribute_name = ?,
             configuration_metadata = ? WHERE registration_id = ?
            """;
    private final JdbcOperations jdbcOperations;
    private final Function<ClientRegistration, List<SqlParameterValue>> clientRegistrationListParametersMapper;
    private final RowMapper<ClientRegistration> clientRegistrationRowMapper;


    public JdbcClientRegistrationRepository(
            JdbcOperations jdbcOperations,
            Map<CommonOAuth2Provider, List<ClientRegistration>> clientRegistrationMap
    ) {
        Assert.notNull(jdbcOperations, "JdbcOperations can not be null");
        this.jdbcOperations = jdbcOperations;
        this.clientRegistrationRowMapper = new ClientRegistrationRowMapper();
        this.clientRegistrationListParametersMapper = new ClientRegistrationParametersMapper();

        clientRegistrationMap.forEach((provider, clientRegistrations) ->
            clientRegistrations.forEach(c -> this.persist(c,provider))
        );
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        Assert.hasText(registrationId, "registrationId cannot be empty");
        return this.findBy("registration_id = ?", registrationId);
    }

    private ClientRegistration findBy(String filter, Object... args) {
        var result = this.jdbcOperations.query(LOAD_CLIENT_REGISTERED_QUERY_SQL + filter, this.clientRegistrationRowMapper, args);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public void delete(String registrationId) {
        Assert.hasText(registrationId, "registrationId cannot be empty");
        var deletionResult = this.jdbcOperations.update("DELETE FROM " + TABLE_NAME + " WHERE registration_id = ?", registrationId);

        log.info("Client registration with registrationId {} deleted, affected rows {}", registrationId, deletionResult);
    }

    private void persist(
            ClientRegistration clientRegistration,
            CommonOAuth2Provider provider
    ) {
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        Assert.notNull(provider, "provider cannot be null");

        var existingClientRegistration = this.findByRegistrationId(clientRegistration.getRegistrationId());

        if (existingClientRegistration != null) {
            this.updateRegisteredClient(clientRegistration);
        } else {
            this.insertClientRegistration(clientRegistration, provider);
        }

    }

    public void updateRegisteredClient(ClientRegistration clientRegistration) {
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        var parameterValues = new ArrayList<>(this.clientRegistrationListParametersMapper.apply(clientRegistration));
        var id = parameterValues.remove(0);
        parameterValues.add(id);
        var statementSetter = new ArgumentPreparedStatementSetter(parameterValues.toArray());
        this.jdbcOperations.update(UPDATE_CLIENT_REGISTERED_SQL, statementSetter);
    }

    public void insertClientRegistration(
            ClientRegistration clientRegistration,
            CommonOAuth2Provider provider
    ) {
        var parameterValues = this.clientRegistrationListParametersMapper.apply(clientRegistration);
        parameterValues.add(new SqlParameterValue(12, provider.name()));

        var statementSetter = new ArgumentPreparedStatementSetter(parameterValues.toArray());
        this.jdbcOperations.update(INSERT_CLIENT_REGISTERED_SQL, statementSetter);
        this.updateProviderName(provider, clientRegistration.getRegistrationId());
    }

    private void updateProviderName(CommonOAuth2Provider providerName, String registrationId) {
        jdbcOperations.update(
                "update " + TABLE_NAME + " set provider_name = ? where registration_id =?",
                providerName.name(), registrationId
        );
    }

    private CommonOAuth2Provider getProviderName(String registrationId) {
        return jdbcOperations.queryForObject(
                "select provider_name from " + TABLE_NAME + " where registration_id = ?",
                CommonOAuth2Provider.class,
                registrationId
        );
    }

    @Override
    @Nonnull
    public Iterator<ClientRegistrationResponse> iterator() {
        return this.jdbcOperations
                .query(LOAD_CLIENT_REGISTERED_SQL, this.clientRegistrationRowMapper).stream()
                .map(c -> new ClientRegistrationResponse(c, this.getProviderName(c.getRegistrationId())))
                .iterator();
    }
}