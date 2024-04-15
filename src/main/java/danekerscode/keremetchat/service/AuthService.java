package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;

public interface AuthService {

    void register(RegistrationRequest request);

    User login(LoginRequest loginRequest);
}
