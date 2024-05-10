package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.ChatUserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private ChatUserRole role;
    @ManyToOne
    private Chat chat;
}
