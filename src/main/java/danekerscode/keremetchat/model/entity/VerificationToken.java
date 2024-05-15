package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.VerificationTokenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class VerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private VerificationTokenType type;
    private LocalDateTime expirationDate;
    @Column(unique = true)
    private String value;
}
