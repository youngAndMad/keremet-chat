package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class SecurityRole extends BaseEntity implements
        GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SecurityRoleType type;

    @Override
    public String getAuthority() {
        return type.name();
    }
}
