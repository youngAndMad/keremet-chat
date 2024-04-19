package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityRoleRepository extends CommonRepository<SecurityRole, Long> {

    @Override
    default Class<SecurityRole> getEntityClass() {
        return SecurityRole.class;
    }
}