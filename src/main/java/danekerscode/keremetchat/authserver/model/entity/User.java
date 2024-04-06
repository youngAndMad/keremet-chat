package danekerscode.keremetchat.authserver.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import danekerscode.keremetchat.authserver.model.enums.AuthType;
import danekerscode.keremetchat.authserver.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @JsonIgnore
    private String password;
    private LocalDateTime registeredTime;
    @Enumerated(EnumType.STRING)
    private AuthType authType;
    private String providerId;
    private String name;
    private String imageUrl;
    private String profileDescription;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private RoleType role;

}
