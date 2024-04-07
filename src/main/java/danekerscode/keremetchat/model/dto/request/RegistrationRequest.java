package danekerscode.keremetchat.model.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotEmpty String username,
        @Email String email,
        @Size(min = 8, max = 30) String password
) {
}
