package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.entity.VerificationToken;
import danekerscode.keremetchat.model.enums.VerificationTokenType;
import danekerscode.keremetchat.repository.VerificationTokenRepository;
import danekerscode.keremetchat.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final AppProperties appProperties;

    @Override
    public void saveForUserWithType(
            User user,
            VerificationTokenType type,
            String value
    ) {
        var verificationTokenTtl = appProperties.getMail().getVerificationTokenTtl();
        var verificationTokenExpiration = LocalDateTime.now().plus(verificationTokenTtl);

        var verificationToken = new VerificationToken();

        verificationToken.setExpirationDate(verificationTokenExpiration);
        verificationToken.setValue(value);
        verificationToken.setUser(user);
        verificationToken.setType(type);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void clearForUserAndType(User user, VerificationTokenType type) {
        verificationTokenRepository.deleteAllByTypeAndUserId(type, user.getId());
    }

    @Override
    public Optional<VerificationToken> findByValueAndType(String value, VerificationTokenType type) {
        return verificationTokenRepository.findByValueAndType(value,type);
    }
}
