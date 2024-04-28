package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@FieldNameConstants
public class SecurityRole extends BaseEntity implements
        GrantedAuthority {

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
