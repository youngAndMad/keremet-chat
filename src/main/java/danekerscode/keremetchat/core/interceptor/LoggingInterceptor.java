package danekerscode.keremetchat.core.interceptor;

import danekerscode.keremetchat.core.AppConstants;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        request.setAttribute(AppConstants.REQUEST_START_TIME.getValue(), System.currentTimeMillis());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView
    ) throws Exception {
        var currentUser = UserContextHolder.getContext();

        var currentUserName = currentUser == null ? null :
                currentUser.getEmail() == null ? currentUser.getUsername() :
                        currentUser.getEmail();

        var totalExecution = System.currentTimeMillis() - (long) request.getAttribute(AppConstants.REQUEST_START_TIME.getValue());

        log.info("Completed api request {}, method ={}, response status = {}, execution = {}ms, user={}" ,
                request.getRequestURI(),
                request.getMethod(),
                response.getStatus(),
                totalExecution,
                currentUserName
        );
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}
