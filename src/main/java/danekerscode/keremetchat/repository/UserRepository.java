package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.AuthType;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :id")
    void deactivateUser(Long id);

    @Modifying
    @Query("DELETE User u where u.isActive=false")
    int deleteAllByActiveFalse();

    boolean existsByUsernameAndProvider(String username, AuthType provider);
}