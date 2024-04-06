package danekerscode.keremetchat.authserver.service.impl;

import danekerscode.keremetchat.authserver.model.dto.request.OtpRequest;
import danekerscode.keremetchat.authserver.model.entity.Otp;
import danekerscode.keremetchat.authserver.model.entity.User;
import danekerscode.keremetchat.authserver.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final SecureRandom secureRandom = new SecureRandom();


    @Override
    public void sendForUser(User user) {
        var otp = new Otp();
        otp.setOtp(generateOtp());
        otp.setUser(user);

    }

    @Override
    public void verifyOtp(OtpRequest otpRequest) {

    }

    private String generateOtp() {
        return String.valueOf(secureRandom.nextInt(100_000, 999_999));
    }
}
