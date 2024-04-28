package danekerscode.keremetchat.core.aspect;

import danekerscode.keremetchat.core.helper.UserContextHelper;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class FetchUserContextAspect {

    private final UserContextHelper userContextHelper;

    @Before("@annotation(danekerscode.keremetchat.core.annotation.FetchUserContext)")
    public void beforeFetchUserContext() {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuth == null || currentAuth.getPrincipal() == null) {
            log.warn("User context is empty");
            return;
        }

        var user = userContextHelper.extractUser(currentAuth);

        UserContextHolder.setContext(user);
    }

    @After("@annotation(danekerscode.keremetchat.core.annotation.FetchUserContext)")
    public void afterFetchUserContext() {
        UserContextHolder.clear();
    }

}