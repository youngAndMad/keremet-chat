package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;

public interface AuthService {

    void register(RegistrationRequest request);

}
