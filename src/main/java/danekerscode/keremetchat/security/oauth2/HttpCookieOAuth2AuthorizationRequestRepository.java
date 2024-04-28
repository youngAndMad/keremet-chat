package danekerscode.keremetchat.security.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import danekerscode.keremetchat.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import  danekerscode.keremetchat.core.AppConstants;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, AppConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME.getValue())
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, AppConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME.getValue());
            CookieUtils.deleteCookie(request, response, AppConstants.REDIRECT_URI_PARAM_COOKIE_NAME.getValue());
            return;
        }

        CookieUtils.addCookie(response, AppConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME.getValue(),
                CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        var redirectUriAfterLogin = request.getParameter(AppConstants.REDIRECT_URI_PARAM_COOKIE_NAME.getValue());

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, AppConstants.REDIRECT_URI_PARAM_COOKIE_NAME.getValue(), redirectUriAfterLogin, cookieExpireSeconds);
        }

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, AppConstants.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME.getValue());
        CookieUtils.deleteCookie(request, response, AppConstants.REDIRECT_URI_PARAM_COOKIE_NAME.getValue());
    }
}
