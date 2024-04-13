package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.mapper.UserMapper;
import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.dto.response.TokenResponse;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.RoleType;
import danekerscode.keremetchat.model.enums.TokenType;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.model.exception.OtpException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.security.internal.JwtService;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.service.OtpService;
import danekerscode.keremetchat.service.RoleService;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final OtpService otpService;

    private final JwtService jwtService;
    private final UserService userService;
    private final RoleService roleService;

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

        var user = userRepository.save(mappedUser);

        otpService.sendForUser(user);
    }

    @Override
    @Transactional
    public TokenResponse confirmEmail(EmailConfirmationRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthProcessingException("User not found", HttpStatus.NOT_FOUND));

        if (user.getEmailVerified()) {
            throw new AuthProcessingException("Email already verified", HttpStatus.BAD_REQUEST);
        }

        var otpList = otpService.getOtpForUserEmail(request.email());

        if (otpList.isEmpty()) {
            throw new OtpException(OtpException.OtpExceptionType.OTP_NOT_FOUND);
        }

        var otp = otpList.get(0);

        if (!otp.getOtp().equals(request.otp())) {
            throw new OtpException(OtpException.OtpExceptionType.OTP_INVALID);
        }

        if (otp.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new OtpException(OtpException.OtpExceptionType.OTP_EXPIRED);
        }

        user.setEmailVerified(true);

        var savedUser = userRepository.save(user);
        this.otpService.clearFor(savedUser.getEmail());

        var role = roleService.addForUser(savedUser, RoleType.ROLE_USER);
        savedUser.setRoles(List.of(role));

        return this.generateToken(savedUser);
    }

    @Override
    public void resendOtp(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthProcessingException("User not found", HttpStatus.NOT_FOUND)); // todo create exception

        otpService.sendForUser(user);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        var passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password()
        );
        var authentication = authenticationManager.authenticate(passwordAuthenticationToken);

        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        var user = userService.findByEmail(loginRequest.email());
        return this.generateToken(user);
    }

    @Override
    public void logout() {
        otpService.clearFor(UserContextHolder.getContext().getEmail());
        SecurityContextHolder.clearContext();
    }

    @Override
    public TokenResponse resetPassword(ResetPasswordRequest request, User user) {
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new AuthProcessingException("Invalid old password", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        return this.generateToken(userRepository.save(user));
    }

    private TokenResponse generateToken(User user) {
        return new TokenResponse(
                jwtService.generateToken(user, TokenType.ACCESS),
                jwtService.generateToken(user, TokenType.REFRESH)
        );
    }
}
