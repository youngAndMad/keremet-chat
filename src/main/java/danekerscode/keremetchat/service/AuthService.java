package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.response.TokenResponse;

public interface AuthService {

    void register(RegistrationRequest request);

    TokenResponse confirmEmail(EmailConfirmationRequest request);

    void resendOtp(String email);
}
