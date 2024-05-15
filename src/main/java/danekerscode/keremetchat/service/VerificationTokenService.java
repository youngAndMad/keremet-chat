package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.VerificationTokenType;

public interface VerificationTokenService {

    void saveForUserWithType(User user, VerificationTokenType type, String value);

    void deleteByValue(String verificationTokenValue);

    void clearForUserAndType(User user, VerificationTokenType type);
}
