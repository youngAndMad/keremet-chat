package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.AppConstants;
import danekerscode.keremetchat.core.mapper.UserMapper;
import danekerscode.keremetchat.core.specs.UserSpecs;
import danekerscode.keremetchat.model.dto.request.UsersCriteria;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.service.AuthTypeService;
import danekerscode.keremetchat.service.SecurityRoleService;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthTypeService authTypeService;
    private final UserRepository userRepository;
    private final SecurityRoleService securityRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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

        var securityRoles = Arrays.stream(SecurityRoleType.values())
                .map(securityRoleService::findByType)
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
    public void deleteUser(Long id, User currentUser) {
        var userExists = this.userRepository.existsById(id);

        if (!userExists) {
            throw new EntityNotFoundException(User.class, id);
        }

        validatePrivateActionAccess(currentUser, id, "You can't delete yourself");

        this.userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id, User currentUser) {
        var userExists = this.userRepository.existsById(id);

        if (!userExists) {
            throw new EntityNotFoundException(User.class, id);
        }

        validatePrivateActionAccess(currentUser, id, "You can't deactivate yourself");

        this.userRepository.deactivateUser(id);
    }

    @Override
    public Page<UserResponseDto> filterUsers(
            UsersCriteria criteria,
            PageRequest pageRequest
    ) {
        var specs = UserSpecs.fromUsersCriteria(criteria);
        return userRepository.findAll(specs, pageRequest)
                .map(this.userMapper::toResponseDto);
    }

    private static void validatePrivateActionAccess(
            User currentUser,
            Long userId,
            String message
    ) {
        if (!(currentUser.getId().equals(userId) ||
                currentUser.getRoles().stream()
                        .noneMatch(
                                role -> role.getType()
                                        .equals(SecurityRoleType.ROLE_APPLICATION_ROOT_ADMIN)
                        )
        )) {
            throw new RuntimeException(message);
        }
    }
}
