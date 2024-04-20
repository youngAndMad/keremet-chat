package danekerscode.keremetchat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import danekerscode.keremetchat.security.JdbcClientRegistrationRepository;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


public class ClientRegistrationParametersMapper implements Function<ClientRegistration, List<SqlParameterValue>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int STRING_SQL_TYPE = 12;

    public ClientRegistrationParametersMapper() {
        var classLoader = JdbcClientRegistrationRepository.class.getClassLoader();
        var securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
    }

    @Override
    public List<SqlParameterValue> apply(ClientRegistration clientRegistration) {
        return Arrays.asList(
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getRegistrationId()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getClientId()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getClientSecret()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getClientAuthenticationMethod().getValue()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getAuthorizationGrantType().getValue()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getClientName()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getRedirectUri()),
                new SqlParameterValue(STRING_SQL_TYPE, StringUtils.collectionToCommaDelimitedString(clientRegistration.getScopes())),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getAuthorizationUri()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getTokenUri()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getJwkSetUri()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getIssuerUri()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue()),
                new SqlParameterValue(STRING_SQL_TYPE, clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()),
                new SqlParameterValue(STRING_SQL_TYPE, ObjectMapperUtils.writeMap(clientRegistration.getProviderDetails().getConfigurationMetadata())));
    }


}