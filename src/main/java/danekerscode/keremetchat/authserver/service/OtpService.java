package danekerscode.keremetchat.authserver.service;

import danekerscode.keremetchat.authserver.model.dto.request.OtpRequest;
import danekerscode.keremetchat.authserver.model.entity.User;

public interface OtpService {

    void sendForUser(User user);

    void verifyOtp(OtpRequest otp);
}
