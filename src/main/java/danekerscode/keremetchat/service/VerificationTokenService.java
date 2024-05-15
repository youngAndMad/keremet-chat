package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.entity.VerificationToken;
import danekerscode.keremetchat.model.enums.VerificationTokenType;

import java.util.Optional;

public interface VerificationTokenService {

    void saveForUserWithType(User user, VerificationTokenType type, String value);

    void clearForUserAndType(User user, VerificationTokenType type);

    Optional<VerificationToken> findByValueAndType(String value,VerificationTokenType type);
}
