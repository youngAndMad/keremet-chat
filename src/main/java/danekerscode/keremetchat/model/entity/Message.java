package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.common.AppConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content = AppConstants.EMPTY_STRING.getValue();
    private LocalDateTime sentAt;
    private boolean deleted;
    private boolean edited;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private ChatMember sender;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Message parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> children;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "message_files",
            joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "file_entity_id" , referencedColumnName = "id")
    )
    private List<FileEntity> files;
}
