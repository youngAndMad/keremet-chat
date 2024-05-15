package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.core.AppConstants;
import danekerscode.keremetchat.core.mapper.UserMapper;
import danekerscode.keremetchat.model.dto.SendMailArgs;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.MailMessageType;
import danekerscode.keremetchat.model.enums.VerificationTokenType;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.security.CustomUserDetails;
import danekerscode.keremetchat.service.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthTypeService authTypeService;
    private final SecurityRoleService securityRoleService;
    private final MailService mailService;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final Supplier<String> randomString = UUID.randomUUID()::toString;
    ;

    @Override
    @Transactional
    public UserResponseDto register(RegistrationRequest request) {
        var user = this.registerUser(request, SecurityRoleType.ROLE_USER);
        var verificationTokenValue = randomString.get();

        var sendMailArgs = SendMailArgs.builder()
                .type(MailMessageType.GREETING)
                .properties(Map.of(
                        "link", appProperties.getMail().getVerificationLinkPattern().formatted(verificationTokenValue),
                        "receiverEmail", user.getEmail(),
                        "verificationTokenTtl", appProperties.getMail().getVerificationTokenTtl().toString()
                ))
                .receiverEmail(user.getEmail())
                .build();

        mailService.sendMail(sendMailArgs)
                .thenRunAsync(() ->
                        verificationTokenService.saveForUserWithType(user, VerificationTokenType.MAIL_VERIFICATION, verificationTokenValue)
                );

        return userMapper.toResponseDto(user);
    }

    protected User registerUser(
            RegistrationRequest registrationRequest,
            SecurityRoleType... roles
    ) {
        var userIsExistByEmail = userRepository.existsByEmail(registrationRequest.email());

        if (userIsExistByEmail) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var userIsExistByUsername = userRepository.existsByUsername(registrationRequest.username());

        if (userIsExistByUsername) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var userSecurityRoles = Arrays.stream(roles).map(securityRoleService::findByType)
                .collect(Collectors.toSet());
        var hashPassword = passwordEncoder.encode(registrationRequest.password());

        var mappedUser = userMapper.registrationRequestToUser(registrationRequest, hashPassword);

        mappedUser.setAuthType(authTypeService.getOrCreateByName(AppConstants.MANUAL_AUTH_TYPE.getValue()));
        mappedUser.setRoles(userSecurityRoles);

        return userRepository.save(mappedUser);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        var currentUserExists = UserContextHolder.isExists();

        if (!currentUserExists) {
            throw new AuthProcessingException("Current user does not exists in context holder", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var currentUser = UserContextHolder.getContext();

        return userMapper.toResponseDto(currentUser);
    }

    @Override
    public UserResponseDto login(
            LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        var passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password()
        );

        var authentication = authenticationManager.authenticate(passwordAuthenticationToken);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        securityContextRepository.saveContext(securityContext, request, response);

        var currentUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return userMapper.toResponseDto(currentUserDetails.user());
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

    @Override
    @Transactional
    public UserResponseDto registerManager(RegistrationRequest request) {
        var registerManager = this.registerUser(request, SecurityRoleType.ROLE_APPLICATION_MANAGER, SecurityRoleType.ROLE_USER);
        return userMapper.toResponseDto(registerManager);
    }

}
