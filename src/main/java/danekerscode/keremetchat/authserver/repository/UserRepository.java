package danekerscode.keremetchat.authserver.repository;

import danekerscode.keremetchat.authserver.model.entity.User;
import danekerscode.keremetchat.authserver.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {

    Optional<User> findByEmail(String email);

}