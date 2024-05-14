package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.enums.ChatType;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ChatProjection {
    private Long id;
    private String name;
    private ChatType type;
    private List<ChatMemberProjection> members;

    public ChatMemberProjection memberForUser(Long userId) {
        return members.stream().filter(m -> m.getUser().getId().equals(userId)).findFirst()
                .orElseThrow(() -> new InvalidRequestPayloadException("You are not a member of current chat"));
    }
}
