package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.enums.ChatUserRole;

public interface ChatMemberProjection {
    Long getId();

    ChatUserRole getRole();

    UserProjection getUser();

    default boolean isAdmin(){
        return getRole() == ChatUserRole.ADMIN;
    }
}
