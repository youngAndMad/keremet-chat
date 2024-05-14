package danekerscode.keremetchat.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import danekerscode.keremetchat.model.entity.AuthType;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import danekerscode.keremetchat.model.projection.ChatMemberProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UserResponseDto(
        Long id,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastModifiedDate,
        Boolean isActive,
        AuthType authType,
        Set<SecurityRoleType> roles,
        List<ChatMemberProjection> chatMembers
) {
}
