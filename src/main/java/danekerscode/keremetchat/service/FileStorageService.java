package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.Message;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void save(MultipartFile file , Message message);

    String save(MultipartFile file, Chat chat);
}
