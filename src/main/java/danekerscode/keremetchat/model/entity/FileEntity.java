package danekerscode.keremetchat.model.entity;

import danekerscode.keremetchat.model.enums.FileEntitySource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String target;
    private FileEntitySource source;
    private String extension;
    private long size;
    private String path; // path in minio 
}
