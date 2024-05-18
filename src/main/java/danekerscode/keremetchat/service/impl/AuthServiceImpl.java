package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.context.holder.UserContextHolder;
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
import danekerscode.keremetchat.security.CustomUserDetails;
import danekerscode.keremetchat.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AppProperties appProperties;
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationManager authenticationManager;
    private final SecurityRoleService securityRoleService;
    private final MailService mailService;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final ExecutorService executor;
    private final Supplier<String> randomString = UUID.randomUUID()::toString;

    @Override
    @Transactional
    public CompletableFuture<UserResponseDto> register(RegistrationRequest request) {
        return CompletableFuture.supplyAsync(() -> this.registerUser(request, SecurityRoleType.ROLE_USER))
                .thenCompose(user -> {
                    var verificationTokenValue = randomString.get();

                    var sendMailArgs = SendMailArgs.builder()
                            .type(MailMessageType.GREETING)
                            .properties(Map.of(
                                    "link", appProperties.getMail().getVerificationLinkPattern().formatted(verificationTokenValue),
                                    "receiverEmail", user.getEmail(),
                                    "verificationTokenTtl", appProperties.getMail().getVerificationTokenTtl().toString().replaceAll("PT", "")
                            ))
                            .receiverEmail(user.getEmail())
                            .build();

                    var tokenSavedFuture = CompletableFuture.runAsync(() ->
                            verificationTokenService.saveForUserWithType(user, VerificationTokenType.MAIL_VERIFICATION, verificationTokenValue), executor
                    );

                    return CompletableFuture.allOf(mailService.sendMail(sendMailArgs), tokenSavedFuture)
                            .thenApply((_void) -> user);
                }).thenApply(userMapper::toResponseDto);


    }

    protected User registerUser(
            RegistrationRequest registrationRequest,
            SecurityRoleType... roles
    ) {
        var userIsExistByEmail = userService.existsByEmail(registrationRequest.email());

        if (userIsExistByEmail) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var userIsExistByUsername = userService.existsByUsername(registrationRequest.username());

        if (userIsExistByUsername) {
            throw new AuthProcessingException("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        var userSecurityRoles = Arrays.stream(roles).map(securityRoleService::findByType)
                .collect(Collectors.toSet());
        var hashPassword = passwordEncoder.encode(registrationRequest.password());

        var mappedUser = userMapper.registrationRequestToUser(registrationRequest, hashPassword);

        mappedUser.setRoles(userSecurityRoles);

        return userService.save(mappedUser);
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

        var authentication = this.authenticateAndCreateSession(passwordAuthenticationToken, request, response);
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
        userService.save(currentUser);
    }

    @Override
    @Transactional
    public UserResponseDto registerManager(RegistrationRequest request) {
        var registerManager = this.registerUser(request, SecurityRoleType.ROLE_APPLICATION_MANAGER, SecurityRoleType.ROLE_USER);
        return userMapper.toResponseDto(registerManager);
    }

    @Override
    public void verifyEmailByToken(String verificationTokenValue, HttpServletRequest request, HttpServletResponse response) {
        var verificationToken = verificationTokenService
                .findByValueAndType(verificationTokenValue, VerificationTokenType.MAIL_VERIFICATION)
                .orElseThrow(() -> new AuthProcessingException("Invalid email verification token passed", HttpStatus.UNAUTHORIZED));

        if (verificationToken.getExpirationDate().isAfter(LocalDateTime.now())) {
            log.warn(
                    "Email verification failed for user {}, verification token has been expired at {}",
                    verificationToken.getUser().getEmail(),
                    verificationToken.getExpirationDate()
            );
            throw new AuthProcessingException("Verification token has been expired at %s"
                    .formatted(verificationToken.getExpirationDate().toString()), HttpStatus.BAD_REQUEST
            );
        }

        var user = verificationToken.getUser();
        user.setEmailConfirmed(true);
        userService.save(user);

        verificationTokenService.clearForUserAndType(user, VerificationTokenType.MAIL_VERIFICATION);
        log.info("Successfully confirmed email {}", user.getEmail());

        var passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        );

        this.authenticateAndCreateSession(passwordAuthenticationToken, request, response);
    }

    private Authentication authenticateAndCreateSession(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        var auth = authenticationManager.authenticate(authentication);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        securityContextRepository.saveContext(securityContext, request, response);

        log.info("Authenticated and created session for {}", auth.getName());
        return auth;
    }


}
