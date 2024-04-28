package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.common.mapper.UserMapper;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.security.CustomUserDetails;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.service.AuthTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthTypeService authTypeService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User register(RegistrationRequest request) {
        var userIsExistByEmail = userRepository.existsByEmail(request.email());

        if (userIsExistByEmail) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var userIsExistByUsername = userRepository.existsByUsername(request.username());

        if (userIsExistByUsername) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var mappedUser = userMapper.registrationRequestToUser(request, passwordEncoder.encode(request.password()));
        mappedUser.setAuthType(authTypeService.getOrCreateByName(AppConstants.MANUAL_AUTH_TYPE.getValue()));
        return userRepository.save(mappedUser);
    }

    @Override
    public User login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        var passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password()
        );

        var authentication = authenticationManager.authenticate(passwordAuthenticationToken);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        securityContextRepository.saveContext(securityContext, request, response);

        var currentUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return currentUserDetails.user();
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        var currentUser = UserContextHolder.getContext();

        if (!passwordEncoder.matches(resetPasswordRequest.oldPassword(), currentUser.getPassword())) {
            throw new AuthProcessingException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        currentUser.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(currentUser);
    }

}
