package danekerscode.keremetchat.model.dto.request;

import danekerscode.keremetchat.core.annotation.Password;

public record ResetPasswordRequest(
        @Password
        String oldPassword,
        @Password
        String newPassword
) {
}
