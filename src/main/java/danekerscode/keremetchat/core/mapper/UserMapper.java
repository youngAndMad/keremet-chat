package danekerscode.keremetchat.core.mapper;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.response.*;
import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    @Mapping(target = "password", expression = "java(hashPassword)")
    User registrationRequestToUser(RegistrationRequest request, String hashPassword);

    @Mapping(target = "roles", expression = "java(extractRoleTypes(user))")
    UserResponseDto toResponseDto(User user);

    default Set<SecurityRoleType> extractRoleTypes(User user){
        var finalRoles = user.getRoles() == null ? new HashSet<SecurityRole>() : user.getRoles();

        return finalRoles.stream().map(SecurityRole::getType)
                .collect(Collectors.toSet());
    }
}
