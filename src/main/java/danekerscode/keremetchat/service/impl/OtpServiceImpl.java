package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.EmailMessageDto;
import danekerscode.keremetchat.model.entity.Otp;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.repository.OtpRepository;
import danekerscode.keremetchat.service.MailService;
import danekerscode.keremetchat.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final MailService mailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.otp.expiration}")
    private Duration otpExpiration;

    @Override
    @Transactional
    public void sendForUser(User user) {
        this.clearFor(user.getEmail());

        log.info("cleaned up old otp for {}", user.getEmail());

        var otp = new Otp();

        otp.setOtp(generateOtp()); // todo convert to mapstruct
        otp.setUser(user);
        otp.setExpireDate(LocalDateTime.now().plus(otpExpiration));

        var emailMessage = new EmailMessageDto(
                user.getEmail(),
                EmailMessageDto.EmailMessageType.OTP,
                Map.of(
                        "otp", otp.getOtp(),
                        "expireDate", otp.getExpireDate().toString()
                )
        );


        this.mailService
                .send(emailMessage)
                .thenRun(() -> {
                    log.info("Successfully sent otp for {}, valid until {}",
                            user.getEmail(), otp.getExpireDate());
                    otp.setSentDate(LocalDateTime.now());
                    otpRepository.save(otp);
                });
    }

    @Override
    public List<Otp> getOtpForUserEmail(String userEmail) {
        return List.copyOf(otpRepository.findAllByUserEmail(userEmail));
    }

    @Override
    public void clearFor(String email) {
        otpRepository.deleteByUserEmail(email);
    }

    private String generateOtp() {
        return String.valueOf(secureRandom.nextInt(100_000, 999_999));
    }
}
