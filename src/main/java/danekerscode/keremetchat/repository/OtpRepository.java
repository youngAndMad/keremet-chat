package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Otp;
import danekerscode.keremetchat.repository.common.CommonRepository;
import jakarta.mail.search.SearchTerm;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OtpRepository extends CommonRepository<Otp, Long> {
    @Override
    default Class<Otp> getEntityClass() {
        return Otp.class;
    }

    void deleteByUserEmail(String userEmail);

    Set<Otp> findAllByUserEmail(String userEmail);
}