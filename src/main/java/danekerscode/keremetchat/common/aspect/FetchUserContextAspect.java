package danekerscode.keremetchat.common.aspect;

import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class FetchUserContextAspect {

    private final UserRepository userRepository;

    @Before("@annotation(danekerscode.keremetchat.common.annotation.FetchUserContext)")
    public void beforeFetchUserContext() {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuth == null || currentAuth.getPrincipal() == null) {
            log.warn("User context is empty");
            return;
        }

        var currentAuthPrincipal = currentAuth.getPrincipal();

        if (currentAuthPrincipal instanceof OAuth2AuthenticationToken oauth2Token) {
            var username = oauth2Token.getPrincipal().getName();

            var optionalUser = userRepository.findByUsername(username);

            optionalUser.ifPresent(user -> {
                log.info("User context is set: {} for OAuth2AuthenticationToken", username);
                UserContextHolder.setContext(user);
            });
            return;
        }


        if (currentAuthPrincipal instanceof UsernamePasswordAuthenticationToken usernamePasswordToken) {
            var email = usernamePasswordToken.getName();

            var optionalUser = userRepository.findByEmail(email);

            optionalUser.ifPresent(user -> {
                log.info("User context is set: {} for UsernamePasswordAuthenticationToken", email);
                UserContextHolder.setContext(user);
            });
            return;
        }

        if (currentAuthPrincipal instanceof String email) {
            var optionalUser = userRepository.findByEmail(email);

            optionalUser.ifPresent(user -> {
                log.info("User context is set: {} for String", email);
                UserContextHolder.setContext(user);
            });
            return;
        }
        log.warn("Not supported authentication type: {}", currentAuthPrincipal.getClass().getSimpleName());
    }

}
