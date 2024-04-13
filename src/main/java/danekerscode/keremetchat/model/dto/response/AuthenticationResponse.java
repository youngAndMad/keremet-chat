package danekerscode.keremetchat.model.dto.response;

import danekerscode.keremetchat.model.entity.User;

public record AuthenticationResponse(
    User user,
    TokenResponse token
) {
}
