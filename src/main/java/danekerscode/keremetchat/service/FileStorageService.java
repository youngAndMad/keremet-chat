package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.response.DownloadFileResponse;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface FileStorageService {

    FileEntity save(MultipartFile file, FileEntitySource source, String target);

    void deleteFile(Long fileId);

    FileEntity findById(Long fileEntityId);

    CompletableFuture<DownloadFileResponse> downloadFile(Long fileEntityId);

    CompletableFuture<DownloadFileResponse> downloadFileWithPath(String objectPath);
}
