package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRoleRepository extends CommonRepository<SecurityRole, Long> {

    Optional<SecurityRole> findByType(SecurityRoleType type);

    @Override
    default Class<SecurityRole> getEntityClass() {
        return SecurityRole.class;
    }
}