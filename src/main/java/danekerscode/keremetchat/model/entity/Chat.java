package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.ChatType;
import danekerscode.keremetchat.model.exception.InvalidRequestPayloadException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.Opt;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ChatType type;
    private String name;

    @OneToOne
    private ChatSettings settings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "chat_avatars",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "file_entity_id")
    )
    private List<FileEntity> avatars;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ChatMember> members;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public ChatMember memberForUser(Long userId) {
        var e = new InvalidRequestPayloadException("You are not a member of current chat");

        if (members == null) {
            throw e;
        }

        return members.stream().filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> e);
    }
}
