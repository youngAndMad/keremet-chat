package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.core.AppConstant;
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

    private String content = AppConstant.EMPTY_STRING.getValue();
    private LocalDateTime sentAt;
    private boolean deleted;
    private boolean edited;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private ChatMember sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Message parent;

    @OneToMany(mappedBy = "parent")
    private List<Message> children;

    @OneToMany
    @JoinTable(
            name = "message_files",
            inverseJoinColumns = @JoinColumn(name = "file_entity_id", referencedColumnName = "id")
    )
    private List<FileEntity> files;
}
