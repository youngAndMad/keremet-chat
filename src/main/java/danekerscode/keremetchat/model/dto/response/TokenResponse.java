package danekerscode.keremetchat.model.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
