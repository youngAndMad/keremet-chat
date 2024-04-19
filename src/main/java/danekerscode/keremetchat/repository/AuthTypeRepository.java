package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.AuthType;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTypeRepository extends CommonRepository<AuthType, Long> {

    Optional<AuthType> findByNameIgnoreCase(String name);

    @Override
    default Class<AuthType> getEntityClass() {
        return AuthType.class;
    }
}