package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.service.AuthTypeService;
import danekerscode.keremetchat.service.SecurityRoleService;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthTypeService authTypeService;
    private final UserRepository userRepository;
    private final SecurityRoleService securityRoleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void checkApplicationRootUser(String email, String password) {
        var applicationRootUserAlreadyExists = this.existsByEmail(email);

        if (applicationRootUserAlreadyExists) {
            log.info("Application root admin already exists. Email: {},  Skipping inserting", email);
            return;
        }

        var securityRoles = Arrays.stream(SecurityRoleType.values()).map(securityRoleService::findByType)
                .collect(Collectors.toSet());

        var user = new User();
        user.setAuthType(authTypeService.getOrCreateByName(AppConstants.MANUAL_AUTH_TYPE.getValue()));
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(email);
        user.setRoles(securityRoles);

        userRepository.save(user);

        log.info("Application root admin registered. Email = {}", email);
    }

    @Override
    public void deleteUser(Long id) {
        var userExists = this.userRepository.existsById(id);

        if (!userExists){
            throw new EntityNotFoundException(User.class,id);
        }

        this.userRepository.deleteById(id);
    }
}
