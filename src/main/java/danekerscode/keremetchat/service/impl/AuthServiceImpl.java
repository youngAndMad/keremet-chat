package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.AuthType;
import danekerscode.keremetchat.model.enums.RoleType;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final OtpService otpService;

    @Override
    public void register(RegistrationRequest request) {
        var userIsExist = userRepository.existsByEmail(request.email());

        if (userIsExist) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        // Register user

        var user = new User(); // todo convert to mapstruct
        user.setEmail(request.email());
        user.setRole(RoleType.ROLE_USER);
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setIsActive(true);
        user.setEmailVerified(true); // todo add email verification
        user.setAuthType(AuthType.MANUAL);

        userRepository.save(user);
//        otpService.sendForUser(user);
    }
}
