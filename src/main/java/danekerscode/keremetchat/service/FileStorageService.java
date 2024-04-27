package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileEntity save(MultipartFile file, FileEntitySource source, String target);

    void deleteFile(Long fileId);

    FileEntity findById(Long fileEntityId);
}
