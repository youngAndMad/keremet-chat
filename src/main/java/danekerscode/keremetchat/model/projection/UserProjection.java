package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.entity.AuthType;

import java.util.List;

public interface UserProjection {
    String getEmail();

    String getUsername();

    Long getId();

    AuthType getAuthType();

    Boolean getIsActive();

//    List<ChatMember> getChatMembers();
}
