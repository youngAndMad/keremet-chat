package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Otp;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends CommonRepository<Otp, Long> {
}