package danekerscode.keremetchat.core.aspect;

import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.core.annotation.FetchUserContext;
import danekerscode.keremetchat.core.helper.UserContextHelper;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class FetchUserContextAspect {

    private final UserContextHelper userContextHelper;

    @Before("@annotation(fetchUserContext)")
    public void beforeFetchUserContext(
            JoinPoint jp,
            FetchUserContext fetchUserContext
    ) {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuth == null || currentAuth.getPrincipal() == null) {
            log.warn("User context is empty");
            return;
        }

        var user = userContextHelper.extractUser(currentAuth);

        UserContextHolder.setContext(user);

        if (Boolean.TRUE.equals(fetchUserContext.checkPersonalAccess())) {
            var accessCheck = fetchUserContext.accessCheck();

            var currentUserId = user.getId();

            boolean currentUserIdIsSame = currentUserId.equals(extractUserIdFromMethodSignature(jp, accessCheck.parameterName()));

            if (!currentUserIdIsSame && !accessCheck.availableForAdmin()) {
                throw new AuthProcessingException("You can not call this endpoint. User id does not match", HttpStatus.FORBIDDEN);
            }
        }
    }

    @After("@annotation(danekerscode.keremetchat.core.annotation.FetchUserContext)")
    public void afterFetchUserContext() {
        UserContextHolder.clear();
    }

    private Number extractUserIdFromMethodSignature(JoinPoint joinPoint, String argName) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var parameterNames = signature.getParameterNames();
        var args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(argName)) {
                var argValue = args[i];
                if (argValue instanceof Number) {
                    return (Number) argValue;
                } else {
                    throw new IllegalArgumentException("User id should be number");
                }
            }
        }
        throw new IllegalArgumentException("User id not found in request param");
    }
}
