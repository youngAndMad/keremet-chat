package danekerscode.keremetchat.model.dto.request;

import danekerscode.keremetchat.common.annotation.Password;
import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email String email,
        @Password String password
) {
}
