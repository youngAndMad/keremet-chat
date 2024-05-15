package danekerscode.keremetchat.model.dto;

import danekerscode.keremetchat.model.enums.MailMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendMailArgs {
    private String receiverEmail;
    private MailMessageType type;
    private Map<String,Object> properties;
}
