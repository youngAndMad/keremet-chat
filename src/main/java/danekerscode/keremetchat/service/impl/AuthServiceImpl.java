package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.mapper.UserMapper;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final UserMapper userMapper;

    private final UserService userService;

    @Override
    public void register(RegistrationRequest request) {
        var userIsExistByEmail = userRepository.existsByEmail(request.email());

        if (userIsExistByEmail) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var userIsExistByUsername = userRepository.existsByUsername(request.username());

        if (userIsExistByUsername) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }
        var mappedUser = userMapper.registrationRequestToUser(request, passwordEncoder.encode(request.password()));
        userRepository.save(mappedUser);
    }


    @Override
    public User login(LoginRequest loginRequest) {
        var passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),loginRequest.password()
        );

        var authentication = authenticationManager.authenticate(passwordAuthenticationToken);

        var securityContext = this.securityContextHolderStrategy.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        return userService.findByEmail(loginRequest.email());
    }

}
