package danekerscode.keremetchat.transport.websocket.interceptor;

import danekerscode.keremetchat.context.UserContextHelper;
import danekerscode.keremetchat.context.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExtractInterceptor implements ChannelInterceptor {

    private final UserContextHelper userContextHelper;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, MessageChannel channel) {
        var accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && accessor.getUser() != null && accessor.getUser()
                instanceof Authentication auth
        ) {
            var user = userContextHelper.extractUser(auth);
            UserContextHolder.setContext(user);
        }

        return message;
    }
}


