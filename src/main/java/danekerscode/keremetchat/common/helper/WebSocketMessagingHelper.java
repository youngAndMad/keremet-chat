package danekerscode.keremetchat.common.helper;

import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketMessagingHelper {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private void deliver(
            Object data,
            WebSocketDestination webSocketDestination,
            Long destinationIdentifier
    ) {
        try {
            simpMessagingTemplate.convertAndSend(webSocketDestination.getDestination(), data);

            log.info(
                    "successfully delivered websocket message to {} with destination {}",
                    destinationIdentifier,
                    webSocketDestination.getDestination()
            );

        } catch (MessagingException messagingException) {
            log.error("error during message delivering to {} ",
                    String.format("destinationIdentifier %d, destination %s", destinationIdentifier, webSocketDestination.getDestination()),
                    messagingException
            );
        }
    }

    public void deliver(
            Object data,
            WebSocketDestination webSocketDestination,
            Long... destinationIdentifiers
    ) {
        Arrays.stream(destinationIdentifiers)
                .forEach(destinationIdentifier -> this.deliver(data, webSocketDestination, destinationIdentifier));
    }
}
