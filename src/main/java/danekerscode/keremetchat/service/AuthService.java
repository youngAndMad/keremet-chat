package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.dto.response.TokenResponse;
import danekerscode.keremetchat.model.entity.User;

public interface AuthService {

    void register(RegistrationRequest request);

    TokenResponse confirmEmail(EmailConfirmationRequest request);

    void resendOtp(String email);

    TokenResponse login(LoginRequest loginRequest);

    void logout();

    TokenResponse resetPassword(ResetPasswordRequest request, User user);
}
