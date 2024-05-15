package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.VerificationToken;
import danekerscode.keremetchat.model.enums.VerificationTokenType;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken, Long> {

    void deleteByValue(String value);

    void deleteAllByTypeAndUserId(VerificationTokenType type, Long userId);
}