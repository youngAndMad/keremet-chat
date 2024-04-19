package danekerscode.keremetchat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Table(name = "users")
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    @ManyToOne
    @JoinColumn(name = "auth_type_id" , referencedColumnName = "id")
    private AuthType authType;

    @ManyToMany
    @JoinTable(
            name = "users_security_roles",
            joinColumns = @JoinColumn(name = "user_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "security_role_id" , referencedColumnName = "id")
    )
    private Set<SecurityRole> roles;
}
