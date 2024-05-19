package danekerscode.keremetchat.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.StringUtils;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {
    private static final int STRING_SQL_TYPE = 12;

    public static String extractTableName(
            Class<?> clazz
    ) {
        var table = clazz.getAnnotation(Table.class);
        return table == null ? toSnakeCase(clazz.getSimpleName()) : table.name();
    }

    public static String extractIdColumnName(Class<?> clazz) {
        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
            if (field.isAnnotationPresent(Id.class)) {

                if (field.isAnnotationPresent(Column.class)) {
                    var column = field.getAnnotation(Column.class);
                    return column.name();
                }

                return toSnakeCase(field.getName());
            }
        }

        throw new IllegalArgumentException("No id column found");
    }

    public static List<SqlParameterValue> clientRegistrationToSqlParameterList(
            ClientRegistration clientRegistration
    ) {
        return new ArrayList<>(List.of(
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getRegistrationId())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getClientId())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getClientSecret())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getClientAuthenticationMethod() == null ? null : clientRegistration.getClientAuthenticationMethod().getValue())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getAuthorizationGrantType() == null ? null : clientRegistration.getAuthorizationGrantType().getValue())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getClientName())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getRedirectUri())),
                new SqlParameterValue(STRING_SQL_TYPE, StringUtils.collectionToCommaDelimitedString((clientRegistration.getScopes()))),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getAuthorizationUri())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getTokenUri())), // todo refactor
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getJwkSetUri())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getIssuerUri())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue())),
                new SqlParameterValue(STRING_SQL_TYPE, (clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName())),
                new SqlParameterValue(STRING_SQL_TYPE, (ObjectMapperUtils.writeMap(clientRegistration.getProviderDetails() == null ? null : clientRegistration.getProviderDetails().getConfigurationMetadata())))
        ));
    }

    public static ClientRegistration mapToClientRegistration(
            ResultSet rs
    ) throws SQLException {
        return ClientRegistration.withRegistrationId(rs.getString("registration_id"))
                .clientId(rs.getString("client_id"))
                .clientSecret(rs.getString("client_secret"))
                .clientAuthenticationMethod(Oauth2Utils.resolveClientAuthenticationMethod(rs.getString("client_authentication_method")))
                .authorizationGrantType(Oauth2Utils.resolveAuthorizationGrantType(rs.getString("authorization_grant_type")))
                .clientName(rs.getString("client_name"))
                .redirectUri(rs.getString("redirect_uri"))
                .scope(StringUtils.commaDelimitedListToSet(rs.getString("scopes")))
                .authorizationUri(rs.getString("authorization_uri"))
                .tokenUri(rs.getString("token_uri"))
                .jwkSetUri(rs.getString("jwk_set_uri"))
                .issuerUri(rs.getString("issuer_uri"))
                .providerConfigurationMetadata(ObjectMapperUtils.parseMap(rs.getString("configuration_metadata")))
                .userInfoUri(rs.getString("user_info_uri"))
                .userInfoAuthenticationMethod(Oauth2Utils.resolveUserInfoAuthenticationMethod(rs.getString("user_info_authentication_method")))
                .userNameAttributeName(rs.getString("user_name_attribute_name"))
                .build();
    }

    public static JdbcClient.StatementSpec enrichJdbcStatementForClientRegistration(
            JdbcClient.StatementSpec st,
            ClientRegistration clientRegistration,
            CommonOAuth2Provider provider
    ) {
        st
                .param("registrationId", clientRegistration.getRegistrationId())
                .param("clientId", clientRegistration.getClientId())
                .param("clientSecret", clientRegistration.getClientSecret())
                .param("clientAuthenticationMethod", clientRegistration.getClientAuthenticationMethod().getValue())
                .param("authorizationGrantType", clientRegistration.getAuthorizationGrantType().getValue())
                .param("clientName", clientRegistration.getClientName())
                .param("redirectUri", clientRegistration.getRedirectUri())
                .param("scopes", StringUtils.collectionToCommaDelimitedString(clientRegistration.getScopes()))
                .param("authorizationUri", clientRegistration.getProviderDetails().getAuthorizationUri())
                .param("tokenUri", clientRegistration.getProviderDetails().getTokenUri())
                .param("jwkSetUri", clientRegistration.getProviderDetails().getJwkSetUri())
                .param("issuerUri", clientRegistration.getProviderDetails().getIssuerUri())
                .param("userInfoUri", clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .param("userInfoAuthenticationMethod", clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue())
                .param("userNameAttributeName", clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName())
                .param("configurationMetadata", ObjectMapperUtils.writeMap(clientRegistration.getProviderDetails().getConfigurationMetadata()))
                .param("providerName", provider.name());

        return st;
    }

    private static String toSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }


}
