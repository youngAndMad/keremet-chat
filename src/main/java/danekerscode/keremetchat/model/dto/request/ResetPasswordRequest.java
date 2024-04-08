package danekerscode.keremetchat.model.dto.request;

import danekerscode.keremetchat.common.annotation.Password;

public record ResetPasswordRequest(
        @Password
        String oldPassword,
        @Password
        String newPassword
) {
}
