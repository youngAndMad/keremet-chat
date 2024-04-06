package danekerscode.keremetchat.authserver.service;

import danekerscode.keremetchat.authserver.model.dto.request.RegistrationRequest;

public interface AuthService {

    void register(RegistrationRequest request);

}
