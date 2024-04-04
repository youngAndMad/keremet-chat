package danekerscode.keremetchat.authserver.repository;

import danekerscode.keremetchat.authserver.model.entity.User;
import danekerscode.keremetchat.authserver.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {

    @Override
    default Class<?> entityClass() {
        return User.class;
    }

}