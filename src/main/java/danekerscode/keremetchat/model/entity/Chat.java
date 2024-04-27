package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.ChatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "chat_avatars",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "file_entity_id")
    )
    private List<FileEntity> avatars;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> members;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
}
