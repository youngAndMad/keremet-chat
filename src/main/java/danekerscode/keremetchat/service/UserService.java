package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.UsersCriteria;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserService {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    void checkApplicationRootUser(
        String email,
        String password
    );

    void deleteUser(Long id, User currentUser);

    void deactivateUser(Long id, User currentUser);

    Page<UserResponseDto> filterUsers(UsersCriteria criteria, PageRequest pageRequest);

    int deleteInactiveUsers();

    User findById(Long userId);

    User save(User user);

    boolean existsByUsername(String username);
}
