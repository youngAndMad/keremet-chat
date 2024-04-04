package danekerscode.keremetchat.authserver.model.dto;

/**
 * The EmailMessageDto record represents the structure of an email message.
 */
public record EmailMessageDto(
        String to,
        String subject,
        String text
) {
}