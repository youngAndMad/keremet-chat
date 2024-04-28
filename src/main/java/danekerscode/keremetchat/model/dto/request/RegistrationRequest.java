package danekerscode.keremetchat.model.dto.request;


import danekerscode.keremetchat.core.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegistrationRequest(
        @NotEmpty String username,
        @Email String email,
        @Password String password
) {
}
