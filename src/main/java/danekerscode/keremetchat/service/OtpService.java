package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.Otp;
import danekerscode.keremetchat.model.entity.User;

import java.util.List;

public interface OtpService {

    void sendForUser(User user);

    List<Otp> getOtpForUserEmail(String userEmail);

    void clearFor(String email);
}
