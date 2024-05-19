package danekerscode.keremetchat.security.oauth2;

import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.AuthType;
import danekerscode.keremetchat.repository.JdbcClientRegistrationRepository;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserRepository userRepository; // todo use user service
    private final JdbcClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        if (authentication instanceof DefaultOAuth2User oAuth2User) {
//            var username = oAuth2User.getName();
//
//            if (userRepository.existsByUsername(username)) {
//                return;
//            }
        }

        if (authentication instanceof OAuth2AuthenticationToken auth2AuthenticationToken) {
            var principal = auth2AuthenticationToken.getPrincipal();
            var username = principal.getName();

            var provider = AuthType.fromCommonOauth2Provider(
                    clientRegistrationRepository.getProviderNameForClientRegistration(auth2AuthenticationToken.getAuthorizedClientRegistrationId())
            );
            if (!userRepository.existsByUsernameAndProvider(username, provider)) {
                var user = new User();
                user.setProvider(provider);
                user.setUsername(username);
                userRepository.save(user);
            }
        }
        var targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        this.clearAuthenticationAttributes(request, response);
        super.getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        return CookieUtils
                .getCookie(request, AppConstant.REDIRECT_URI_PARAM_COOKIE_NAME.getValue())
                .map(Cookie::getValue)
                .orElse(AppConstant.DEFAULT_SUCCESS_LOGIN_REDIRECT_URL.getValue());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
