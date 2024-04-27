package danekerscode.keremetchat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import danekerscode.keremetchat.security.oauth2.JdbcClientRegistrationRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRegistrationRowMapper implements RowMapper<ClientRegistration> {
    private static  final ObjectMapper objectMapper = new ObjectMapper();

    public ClientRegistrationRowMapper() {
        var classLoader = JdbcClientRegistrationRepository.class.getClassLoader();
        var securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
    }

    @Override
    public ClientRegistration mapRow(ResultSet rs, int rowNum) throws SQLException {
        var scopes = StringUtils.commaDelimitedListToSet(rs.getString("scopes"));
        var builder = ClientRegistration.withRegistrationId(rs.getString("registration_id"))
                .clientId(rs.getString("client_id"))
                .clientSecret(rs.getString("client_secret"))
                .clientAuthenticationMethod(Oauth2Utils.resolveClientAuthenticationMethod(rs.getString("client_authentication_method")))
                .authorizationGrantType(Oauth2Utils.resolveAuthorizationGrantType(rs.getString("authorization_grant_type")))
                .clientName(rs.getString("client_name"))
                .redirectUri(rs.getString("redirect_uri"))
                .scope(scopes)
                .authorizationUri(rs.getString("authorization_uri"))
                .tokenUri(rs.getString("token_uri"))
                .jwkSetUri(rs.getString("jwk_set_uri"))
                .issuerUri(rs.getString("issuer_uri"))
                .userInfoUri(rs.getString("user_info_uri"))
                .userInfoAuthenticationMethod(Oauth2Utils.resolveUserInfoAuthenticationMethod(rs.getString("user_info_authentication_method")))
                .userNameAttributeName(rs.getString("user_name_attribute_name"));

        var configurationMetadata = ObjectMapperUtils.parseMap(rs.getString("configuration_metadata"));
        builder.providerConfigurationMetadata(configurationMetadata);
        return builder.build();
    }
}