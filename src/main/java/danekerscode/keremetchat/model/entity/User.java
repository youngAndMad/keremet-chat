package danekerscode.keremetchat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import danekerscode.keremetchat.model.enums.AuthType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Table(name = "users")
@Setter
@FieldNameConstants
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private AuthType provider;
    private boolean isActive = true;

    private Boolean emailConfirmed;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<VerificationToken> verificationTokens;

    @ManyToMany
    @JoinTable(
            name = "users_security_roles",
            inverseJoinColumns = @JoinColumn(name = "security_role_id")
    )
    private Set<SecurityRole> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMember> chatMembers;
}
