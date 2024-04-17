package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;

public interface AuthService {

    User register(RegistrationRequest request);

}
