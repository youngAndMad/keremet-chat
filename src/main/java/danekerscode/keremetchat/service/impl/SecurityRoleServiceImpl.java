package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import danekerscode.keremetchat.repository.SecurityRoleRepository;
import danekerscode.keremetchat.service.SecurityRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityRoleServiceImpl implements SecurityRoleService {

    private final SecurityRoleRepository securityRoleRepository;

    @Override
    public SecurityRole findByType(SecurityRoleType securityRoleType) {
        return this.securityRoleRepository
                .findByType(securityRoleType)
                .orElseGet(() -> {
                    var role = new SecurityRole();
                    role.setType(securityRoleType);
                    var savedRole =  securityRoleRepository.save(role);
                    log.info("Created new SecurityRole {}" , securityRoleType);
                    return savedRole;
                });
    }
}
