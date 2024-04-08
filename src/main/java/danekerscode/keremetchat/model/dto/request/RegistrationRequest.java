package danekerscode.keremetchat.model.dto.request;


import danekerscode.keremetchat.common.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotEmpty String username,
        @Email String email,
        @Password String password
) {
}
