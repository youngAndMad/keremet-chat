package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.User;

public interface UserService {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    void checkApplicationRootUser(
        String email,
        String password
    );
}
