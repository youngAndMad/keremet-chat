package danekerscode.keremetchat.model.dto.response;

import danekerscode.keremetchat.model.entity.AuthType;
import danekerscode.keremetchat.model.entity.SecurityRole;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
        Long id,
        String email,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedData,
        Boolean isActive,
        AuthType authType,
        List<SecurityRole> roles
) {
}
