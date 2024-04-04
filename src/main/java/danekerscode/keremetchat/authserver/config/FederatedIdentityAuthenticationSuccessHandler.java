package danekerscode.keremetchat.authserver.config;

import java.io.IOException;
import java.util.function.Consumer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public final class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();

    private final Consumer<OAuth2User> oauth2UserHandler = new UserRepositoryOAuth2UserHandler();
    private final Consumer<OidcUser> oidcUserHandler = new UserRepositoryOidcUserHandler();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    )
            throws IOException, ServletException {

        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            this.delegate.onAuthenticationSuccess(request, response, authentication);
        }

        if (authentication.getPrincipal() instanceof OidcUser) {
            this.oidcUserHandler.accept((OidcUser) authentication.getPrincipal());
        }

        this.oauth2UserHandler.accept((OAuth2User) authentication.getPrincipal());
    }

}