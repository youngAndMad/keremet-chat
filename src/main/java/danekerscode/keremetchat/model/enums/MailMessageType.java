package danekerscode.keremetchat.model.enums;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public enum MailMessageType {
    MAIL_CONFIRMATION("mail_confirmation" , "Mail confirmation for Keremet chat"),
    GREETING("greeting.ftl", "Welcome to Keremet chat by Daneker");

    /*
    * Value of template to send with mail message content in templates folder.
    * Should be passed with file extension
    * */
    private final String templateName;

    /*
    * Title of message which will displayed on preview mode(or on mail notification)
    * */
    private final String subject;

    MailMessageType(String templateName, String subject) {
        Assert.hasText(templateName, "Template for mail message type should contain text");
        Assert.hasText(subject, "Subject for mail message type should contain text");

        this.subject = subject;
        this.templateName = templateName;
    }
}
