package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.EmailMessageDto;
import danekerscode.keremetchat.model.entity.Otp;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.repository.OtpRepository;
import danekerscode.keremetchat.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
//    private final MailService mailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.otp.expiration}")
    private Duration otpExpiration;

    @Override
    public void sendForUser(User user) {
        var otp = new Otp();

        otp.setOtp(generateOtp()); // todo convert to mapstruct
        otp.setUser(user);
        otp.setSentDate(LocalDateTime.now());
        otp.setExpireDate(LocalDateTime.now().plus(otpExpiration));

        otpRepository.save(otp);

        var emailMessage = new EmailMessageDto(
                user.getEmail(),
                EmailMessageDto.EmailMessageType.OTP,
                Map.of(
                        "otp", otp.getOtp(),
                        "expireDate", otp.getExpireDate().toString()
                )
        );

//        this.mailService.send(emailMessage)
//                .join();
    }

    private String generateOtp() {
        return String.valueOf(secureRandom.nextInt(100_000, 999_999));
    }
}
