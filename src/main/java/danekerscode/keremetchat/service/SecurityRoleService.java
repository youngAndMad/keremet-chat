package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;

public interface SecurityRoleService {

    SecurityRole findByType(SecurityRoleType securityRoleType);

}
