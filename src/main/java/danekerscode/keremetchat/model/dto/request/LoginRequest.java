package danekerscode.keremetchat.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email String email,
        @Size(min = 8,max = 30) String password
) {
}
