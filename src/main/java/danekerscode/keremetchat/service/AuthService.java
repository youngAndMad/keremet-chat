package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    User register(RegistrationRequest request);

    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
}
