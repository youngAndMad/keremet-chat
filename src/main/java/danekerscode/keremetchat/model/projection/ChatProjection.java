package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.enums.ChatType;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;

import java.util.List;
import java.util.Optional;

public interface ChatProjection {
    Long getId();

    String getName();

    ChatType getType();

    List<ChatMemberProjection> getMembers();

    default ChatMemberProjection memberForUser(Long userId) {
        return getMembers().stream().filter(m -> m.getUser().getId().equals(userId)).findFirst()
                .orElseThrow(() -> new InvalidRequestPayloadException("You are not a member of current chat"));
    }
}
