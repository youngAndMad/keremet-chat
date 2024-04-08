package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.Role;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.RoleType;

public interface RoleService {

    Role addForUser(User user, RoleType roleType);

}
