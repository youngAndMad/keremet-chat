package danekerscode.keremetchat.core.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketMessagingHelper {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public <T extends Serializable> void deliver(
            T data,
            String webSocketDestination
    ) {
        try {
            simpMessagingTemplate.convertAndSend(webSocketDestination, data);

            log.info(
                    "successfully delivered websocket message with destination {}",
                    webSocketDestination
            );

        } catch (MessagingException messagingException) {
            log.error("error during message delivering to {} ",
                    String.format("destination %s", webSocketDestination),
                    messagingException
            );
        }
    }
}
