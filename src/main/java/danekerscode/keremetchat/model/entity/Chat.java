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
    @OneToMany(mappedBy = "chat")
    private List<ChatMember> members;
}
