package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.enums.ChatType;

public interface ChatProjection {
    Long getId();

    String getName();

    ChatType getType();
}
