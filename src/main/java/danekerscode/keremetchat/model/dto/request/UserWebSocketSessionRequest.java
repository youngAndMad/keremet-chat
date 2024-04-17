package danekerscode.keremetchat.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserWebSocketSessionRequest(
        @NotNull @Positive Long userId
) {
}
