package danekerscode.keremetchat.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import danekerscode.keremetchat.model.enums.AuthType;
import danekerscode.keremetchat.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "users")
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean emailVerified;
    private String username;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private AuthType authType;
    private String imageUrl;
    private String profileDescription;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private RoleType role;

}
