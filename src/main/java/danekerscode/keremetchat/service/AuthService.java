package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.dto.response.AuthenticationResponse;
import danekerscode.keremetchat.model.entity.User;

public interface AuthService {

    void register(RegistrationRequest request);

    AuthenticationResponse confirmEmail(EmailConfirmationRequest request);

    void resendOtp(String email);

    AuthenticationResponse login(LoginRequest loginRequest);

    void logout();

    AuthenticationResponse resetPassword(ResetPasswordRequest request, User user);
}
