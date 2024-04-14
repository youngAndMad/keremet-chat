package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Role;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CommonRepository<Role, Long> {
    @Override
    default Class<Role> getEntityClass() {
        return Role.class;
    }
}