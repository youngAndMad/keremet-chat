package danekerscode.keremetchat.model.dto.response;

import danekerscode.keremetchat.model.entity.AuthType;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDto(
        Long id,
        String email,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate,
        Boolean isActive,
        AuthType authType,
        Set<SecurityRoleType> roles
) {
}
