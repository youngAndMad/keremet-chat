package danekerscode.keremetchat.transport.websocket;

import danekerscode.keremetchat.service.MessageService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/message/{chatId}")
public final class MessageController extends AbstractWebSocketController {

    private final MessageService messageService;

    @MessageMapping
    void sendMessage(
            @DestinationVariable @Positive Long chatId,
            Authentication auth
    ) {
        var user = super.getUserFromAuthentication(auth);

    }

}
