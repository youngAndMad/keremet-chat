package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SecurityRole extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SecurityRoleType type;
}
