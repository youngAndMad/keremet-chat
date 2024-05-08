package danekerscode.keremetchat.security.oauth2;

import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import danekerscode.keremetchat.model.exception.Oauth2ProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final AntPathRequestMatcher authorizationRequestMatcher;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private static final StringKeyGenerator DEFAULT_STATE_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder());
    private static final StringKeyGenerator DEFAULT_SECURE_KEY_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
    private static final Consumer<OAuth2AuthorizationRequest.Builder> DEFAULT_PKCE_APPLIER = OAuth2AuthorizationRequestCustomizers.withPkce();

    public CustomOAuth2AuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository,
            String authorizationRequestBaseUri
    ) {
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        Assert.hasText(authorizationRequestBaseUri, "authorizationRequestBaseUri cannot be empty");

        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizationRequestMatcher = new AntPathRequestMatcher(authorizationRequestBaseUri + "/{registrationId}");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        String registrationId = this.resolveRegistrationId(request);
        if (registrationId == null) {
            throw new Oauth2ProcessingException("Invalid registrationId id in request");
        } else {
            String redirectUriAction = this.getAction(request, "login");
            return this.resolve(request, registrationId, redirectUriAction);
        }
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
        if (registrationId == null) {
            throw new InvalidRequestPayloadException("Invalid registrationId id in request");
        } else {
            String redirectUriAction = this.getAction(request, "authorize");
            return this.resolve(request, registrationId, redirectUriAction);
        }
    }

    private OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId, String redirectUriAction) {
        if (registrationId == null) {
            throw new AuthProcessingException("Invalid registrationId id in request", HttpStatus.BAD_REQUEST);
        }

        var clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);

        if (clientRegistration == null) {
            throw new AuthProcessingException("Invalid registrationId id in request [%s]".formatted(registrationId), HttpStatus.BAD_REQUEST);
        }

        var builder = this.getBuilder(clientRegistration);
        var redirectUriStr = expandRedirectUri(request, clientRegistration, redirectUriAction);
        builder.clientId(clientRegistration.getClientId()).authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri()).redirectUri(redirectUriStr).scopes(clientRegistration.getScopes()).state(DEFAULT_STATE_GENERATOR.generateKey());
        return builder.build();
    }

    private OAuth2AuthorizationRequest.Builder getBuilder(ClientRegistration clientRegistration) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            var builder = OAuth2AuthorizationRequest.authorizationCode().attributes((attrs) -> {
                attrs.put("registration_id", clientRegistration.getRegistrationId());
            });
            if (!CollectionUtils.isEmpty(clientRegistration.getScopes()) && clientRegistration.getScopes().contains("openid")) {
                applyNonce(builder);
            }

            if (ClientAuthenticationMethod.NONE.equals(clientRegistration.getClientAuthenticationMethod())) {
                DEFAULT_PKCE_APPLIER.accept(builder);
            }

            return builder;
        } else {
            String grantType = clientRegistration.getAuthorizationGrantType().getValue();
            throw new IllegalArgumentException("Invalid Authorization Grant Type (" + grantType + ") for Client Registration with Id: " + clientRegistration.getRegistrationId());
        }
    }

    private String getAction(HttpServletRequest request, String defaultAction) {
        var action = request.getParameter("action");
        return action == null ? defaultAction : action;
    }

    private String resolveRegistrationId(HttpServletRequest request) {
        return this.authorizationRequestMatcher.matches(request) ?
                this.authorizationRequestMatcher.matcher(request).getVariables().get("registrationId")
                : null;
    }

    private static String expandRedirectUri(
            HttpServletRequest request,
            ClientRegistration clientRegistration,
            String action
    ) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("registrationId", clientRegistration.getRegistrationId());

        var uriComponents = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null).build();

        var scheme = uriComponents.getScheme();
        uriVariables.put("baseScheme", scheme != null ? scheme : "");
        var host = uriComponents.getHost();
        uriVariables.put("baseHost", host != null ? host : "");
        int port = uriComponents.getPort();
        uriVariables.put("basePort", port == -1 ? "" : ":" + port);
        var path = uriComponents.getPath();

        if (StringUtils.hasLength(path) && path.charAt(0) != '/') {
            path = "/" + path;
        }

        uriVariables.put("basePath", path != null ? path : "");
        uriVariables.put("baseUrl", uriComponents.toUriString());
        uriVariables.put("action", action != null ? action : "");

        return UriComponentsBuilder
                .fromUriString(clientRegistration.getRedirectUri())
                .buildAndExpand(uriVariables)
                .toUriString();
    }

    private static void applyNonce(OAuth2AuthorizationRequest.Builder builder) {
        try {
            var nonce = DEFAULT_SECURE_KEY_GENERATOR.generateKey();
            var nonceHash = createHash(nonce);
            builder.attributes((attrs) -> {
                attrs.put("nonce", nonce);
            });
            builder.additionalParameters((params) -> {
                params.put("nonce", nonceHash);
            });
        } catch (NoSuchAlgorithmException e) {
        }

    }

    private static String createHash(String value) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("SHA-256");
        var digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
