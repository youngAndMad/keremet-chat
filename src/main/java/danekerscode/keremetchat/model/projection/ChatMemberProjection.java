package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.enums.ChatUserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMemberProjection {
    private Long id;
    private ChatUserRole role;
    private UserProjection user;

    public boolean isAdmin(){
        return role == ChatUserRole.ADMIN;
    }
}
