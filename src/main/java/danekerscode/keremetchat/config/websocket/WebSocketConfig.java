package danekerscode.keremetchat.config.websocket;

import danekerscode.keremetchat.config.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final StompSubProtocolErrorHandler STOMP_SUB_PROTOCOL_ERROR_HANDLER = new StompSubProtocolErrorHandler();
    private final AppProperties appProperties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/keremet-chat")
                .setAllowedOriginPatterns(appProperties.getWebsocket().getAllowedOrigins())
                .withSockJS()
                .setSessionCookieNeeded(true);

        registry.setErrorHandler(STOMP_SUB_PROTOCOL_ERROR_HANDLER);
    }

}
