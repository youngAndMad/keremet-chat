package danekerscode.keremetchat.common.helper;

import danekerscode.keremetchat.model.enums.websocket.WebSocketDestinationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketMessagingHelper {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private void deliver(
            Object data,
            WebSocketDestinationType webSocketDestinationType,
            Long destinationIdentifier
    ) {
        try {

            if (!webSocketDestinationType.getDestinationType().isWithIdentifier()) {
                simpMessagingTemplate.convertAndSend(webSocketDestinationType.getDestination(), data);
            } else {
                var destination = webSocketDestinationType.getDestination();
                var finalDestination = destination.replace(Objects.requireNonNull(webSocketDestinationType.getIdentifier()), destinationIdentifier.toString());
                simpMessagingTemplate.convertAndSend(finalDestination, data);
            }

            log.info(
                    "successfully delivered websocket message to {} with destination {}",
                    destinationIdentifier,
                    webSocketDestinationType.getDestination()
            );
        } catch (MessagingException messagingException) {
            log.error("error during message delivering to {} ",
                    String.format("destinationIdentifier %d, destination %s", destinationIdentifier, webSocketDestinationType.getDestination()),
                    messagingException
            );
        }
    }

    public void deliver(
            Object data,
            WebSocketDestinationType webSocketDestinationType,
            Long... destinationIdentifiers
    ) {
        Arrays.stream(destinationIdentifiers)
                .forEach(destinationIdentifier -> this.deliver(data,webSocketDestinationType,destinationIdentifier));
    }
}
