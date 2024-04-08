package danekerscode.keremetchat.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Enumerated(EnumType.STRING)
    private RoleType name;
    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private User user;

}
