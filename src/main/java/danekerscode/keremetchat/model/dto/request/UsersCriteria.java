package danekerscode.keremetchat.model.dto.request;

import danekerscode.keremetchat.model.entity.SecurityRole;

import java.time.LocalDateTime;

public record UsersCriteria(
        LocalDateTime registeredTimeFrom,
        LocalDateTime registeredTimeTo,
        String authType,
        String keyword,
        Boolean isActive,
        SecurityRole role
) {
}
