package danekerscode.keremetchat.model.dto.request;

import danekerscode.keremetchat.core.annotation.Password;
import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email String email,
        @Password String password
) {
}
