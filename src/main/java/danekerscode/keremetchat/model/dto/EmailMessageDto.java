package danekerscode.keremetchat.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The EmailMessageDto record represents the structure of an email message.
 */
public record EmailMessageDto(
        String to,
        EmailMessageType type,
        Map<String, String> varargs
) {

    public EmailMessageDto(
            String to,
            EmailMessageType type
    ) {
        this(
                to, type,
                Collections.emptyMap()
        );
    }

    @RequiredArgsConstructor
    @Getter
    public enum EmailMessageType {
        REGISTRATION_GREETING("registration-greeting.ftl"),
        OTP("otp.ftl"),
        ;

        private final String templateName;
    }
}
