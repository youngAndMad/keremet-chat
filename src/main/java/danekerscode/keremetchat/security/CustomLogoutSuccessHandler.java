package danekerscode.keremetchat.security;

import danekerscode.keremetchat.context.UserContextHelper;
import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.service.OtpService;
import danekerscode.keremetchat.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static danekerscode.keremetchat.common.AppConstants.ACCESS_TOKEN;
import static danekerscode.keremetchat.common.AppConstants.REFRESH_TOKEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserContextHelper userContextHelper;
    private final OtpService otpService;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CookieUtils.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);

        var user = userContextHelper.extractUser(authentication);

        log.info("User {} logged out successfully", user.getUsername());
        otpService.clearFor(user.getEmail());

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
