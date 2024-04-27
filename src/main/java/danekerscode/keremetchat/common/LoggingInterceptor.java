package danekerscode.keremetchat.common;

import org.hibernate.annotations.SoftDelete;
import org.springframework.web.servlet.HandlerInterceptor;

@SoftDelete
public class LoggingInterceptor implements HandlerInterceptor {
}
