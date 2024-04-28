package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    UserResponseDto register(RegistrationRequest request);
    
    UserResponseDto getCurrentUser();

    UserResponseDto login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    UserResponseDto registerManager(RegistrationRequest request);
}
