package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.entity.Role;
import danekerscode.keremetchat.model.entity.RoleRepository;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.RoleType;
import danekerscode.keremetchat.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role addForUser(User user, RoleType roleType) {
        var role = new Role();
        role.setUser(user);
        role.setName(roleType);
        return this.roleRepository.save(role);
    }

}
