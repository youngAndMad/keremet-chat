package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.VerificationToken;
import danekerscode.keremetchat.model.enums.VerificationTokenType;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken, Long> {

    void deleteAllByTypeAndUserId(VerificationTokenType type, Long userId);

    Optional<VerificationToken> findByValueAndType(String value, VerificationTokenType type);
}