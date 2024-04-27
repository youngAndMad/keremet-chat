package danekerscode.keremetchat.model.dto.request;

import jakarta.validation.constraints.Email;

public record CreatePrivateChatRequest(
        @Email String receiverEmail
) {
}
