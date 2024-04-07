package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.mapper.UserMapper;
import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.response.TokenResponse;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.TokenType;
import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.security.internal.JwtService;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final OtpService otpService;

    private final JwtService jwtService;

    @Override
    public void register(RegistrationRequest request) {
        var userIsExist = userRepository.existsByEmail(request.email());

        if (userIsExist) {
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
            throw new AuthProcessingException("Otp not found", HttpStatus.NOT_FOUND); // todo create exception
        }

        var otp = otpList.get(0);

        if (!otp.getOtp().equals(request.otp())) {
            throw new AuthProcessingException("Invalid otp", HttpStatus.BAD_REQUEST);
        }

        if (otp.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new AuthProcessingException("Otp expired", HttpStatus.BAD_REQUEST);
        }

        user.setEmailVerified(true);
        this.otpService.clearFor(user.getEmail());

        return this.generateToken(userRepository.save(user));
    }

    @Override
    public void resendOtp(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthProcessingException("User not found", HttpStatus.NOT_FOUND)); // todo create exception

        otpService.sendForUser(user);
    }

    private TokenResponse generateToken(User user) {
        return new TokenResponse(
                jwtService.generateToken(user, TokenType.ACCESS),
                jwtService.generateToken(user, TokenType.REFRESH)
        );
    }
}
