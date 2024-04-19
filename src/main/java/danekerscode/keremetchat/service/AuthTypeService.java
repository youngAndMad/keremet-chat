package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.AuthType;

public interface AuthTypeService {

    AuthType getOrCreateByName(String name);
}
