package danekerscode.keremetchat.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EmailConfirmationRequest(
        @Email String email,
        @NotEmpty @Size(max = 6,min = 6) String otp
) {
}
