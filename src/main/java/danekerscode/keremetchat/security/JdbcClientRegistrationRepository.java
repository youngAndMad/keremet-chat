package danekerscode.keremetchat.security;

import danekerscode.keremetchat.utils.ClientRegistrationParametersMapper;
import danekerscode.keremetchat.utils.ClientRegistrationRowMapper;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class JdbcClientRegistrationRepository implements ClientRegistrationRepository,
        Iterable<ClientRegistration> {

    private static final String COLUMN_NAMES =
            """
                    registration_id,client_id,client_secret,client_authentication_method,authorization_grant_type,
                    client_name,redirect_uri,scopes,authorization_uri,token_uri,jwk_set_uri,issuer_uri,user_info_uri,
                    user_info_authentication_method,user_name_attribute_name,configuration_metadata
                    """;
    private static final String TABLE_NAME = "oauth2_client_registered";
    private static final String LOAD_CLIENT_REGISTERED_SQL = "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME;
    private static final String LOAD_CLIENT_REGISTERED_QUERY_SQL = LOAD_CLIENT_REGISTERED_SQL + " WHERE ";
    private static final String INSERT_CLIENT_REGISTERED_SQL = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_CLIENT_REGISTERED_SQL = "UPDATE " + TABLE_NAME + " SET client_id = ?,client_secret = ?,client_authentication_method = ?,authorization_grant_type = ?,client_name = ?,redirect_uri = ?,scopes = ?,authorization_uri = ?,token_uri = ?,jwk_set_uri = ?,issuer_uri = ?,user_info_uri = ?,user_info_authentication_method = ?,user_name_attribute_name = ?,configuration_metadata = ? WHERE registration_id = ?";
    private final JdbcOperations jdbcOperations;
    private final Function<ClientRegistration, List<SqlParameterValue>> clientRegistrationListParametersMapper;
    private final RowMapper<ClientRegistration> clientRegistrationRowMapper;


    public JdbcClientRegistrationRepository(JdbcOperations jdbcOperations, Collection<ClientRegistration> clientRegistrations) {
        Assert.notNull(jdbcOperations, "JdbcOperations can not be null");
        this.jdbcOperations = jdbcOperations;
        this.clientRegistrationRowMapper = new ClientRegistrationRowMapper();
        this.clientRegistrationListParametersMapper = new ClientRegistrationParametersMapper();

        clientRegistrations.forEach(this::persist);
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

        log.info("Client registration with registrationId {} deleted, effected rows {}", registrationId, deletionResult);
    }

    public boolean persist(ClientRegistration clientRegistration) {
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        var existingClientRegistration = this.findByRegistrationId(clientRegistration.getRegistrationId());
        if (existingClientRegistration != null) {
            this.updateRegisteredClient(clientRegistration);
            return false;
        } else {
            this.insertClientRegistration(clientRegistration);
            return true;
        }
    }

    private void updateRegisteredClient(ClientRegistration clientRegistration) {
        var parameterValues = new ArrayList<>(this.clientRegistrationListParametersMapper.apply(clientRegistration));
        var id = parameterValues.remove(0);
        parameterValues.add(id);
        var statementSetter = new ArgumentPreparedStatementSetter(parameterValues.toArray());
        this.jdbcOperations.update(UPDATE_CLIENT_REGISTERED_SQL, statementSetter);
    }

    private void insertClientRegistration(ClientRegistration clientRegistration) {
        var parameterValues = this.clientRegistrationListParametersMapper.apply(clientRegistration);
        var statementSetter = new ArgumentPreparedStatementSetter(parameterValues.toArray());
        this.jdbcOperations.update(INSERT_CLIENT_REGISTERED_SQL, statementSetter);
    }

    public List<ClientRegistration> findAny() {
        return this.jdbcOperations.query(LOAD_CLIENT_REGISTERED_SQL, this.clientRegistrationRowMapper);
    }

    @Override
    @Nonnull
    public Iterator<ClientRegistration> iterator() {
        return this.findAny().iterator();
    }
}