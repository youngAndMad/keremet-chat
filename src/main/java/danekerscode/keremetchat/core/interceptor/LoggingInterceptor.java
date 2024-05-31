package danekerscode.keremetchat.core.interceptor;

import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.core.helper.UserContextHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final UserContextHelper userContextHelper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        request.setAttribute(AppConstant.REQUEST_START_TIME.getValue(), System.currentTimeMillis());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView
    ) throws Exception {
        var currentUserName = extractCurrentUsername();

        var totalExecution = System.currentTimeMillis() - (long) request.getAttribute(AppConstant.REQUEST_START_TIME.getValue());

        log.info("Completed api request {}, method = {}, response status = {}, execution = {}ms, user={}",
                request.getRequestURI(),
                request.getMethod(),
                response.getStatus(),
                totalExecution,
                currentUserName
        );
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    private String extractCurrentUsername() {
        var currentUser = UserContextHolder.get();

        var currentUserName = currentUser == null ? null :
                currentUser.getEmail() == null ? currentUser.getUsername() :
                        currentUser.getEmail();

        if (currentUserName != null) {
            return currentUserName;
        }

        try {
            var user = userContextHelper.extractUser(SecurityContextHolder.getContext().getAuthentication());
            return user.getEmail();
        } catch (Exception e) {
            log.error("Failed to extract user from SecurityContextHolder", e);
            return null;
        }
    }

}
