package danekerscode.keremetchat.authserver.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @PrePersist
    void prePersist() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }
}
