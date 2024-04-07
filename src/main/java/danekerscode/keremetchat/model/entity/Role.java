package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private RoleType name;
    @ManyToOne
    @JoinColumn
    private User user;
}
