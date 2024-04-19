package danekerscode.keremetchat.common.helper;

import danekerscode.keremetchat.model.enums.websocket.WebSocketDestination;
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
            WebSocketDestination webSocketDestination,
            Long destinationIdentifier
    ) {
        try {

            if (!webSocketDestination.getDestinationType().isWithIdentifier()) {
                simpMessagingTemplate.convertAndSend(webSocketDestination.getDestination(), data);
            } else {
                var destination = webSocketDestination.getDestination();
                var finalDestination = destination.replace(Objects.requireNonNull(webSocketDestination.getIdentifier()), destinationIdentifier.toString());
                simpMessagingTemplate.convertAndSend(finalDestination, data);
            }

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
                .forEach(destinationIdentifier -> this.deliver(data, webSocketDestination,destinationIdentifier));
    }
}
