package danekerscode.keremetchat.model.dto;

import jakarta.validation.constraints.NotNull;

public record ChatSettingsDto(
        @NotNull
        Boolean everyoneCanInviteMembers,
        @NotNull
        Boolean membersListIsAvailable,
        @NotNull
        Boolean adminsCanEditSettings
) {
}
