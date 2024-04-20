package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.entity.Message;
import danekerscode.keremetchat.repository.FileEntityRepository;
import danekerscode.keremetchat.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileEntityRepository fileEntityRepository;
    private final MinioClient minioClient;

    @Override
    public void save(MultipartFile file, Message message) {
        this
                .uploadFile(file)
                .thenRun(() -> {
                    var fileEntity = new FileEntity();
                    fileEntity.setFileName(file.getOriginalFilename());
                    fileEntity.setExtension(file.getContentType());
                    fileEntity.setSize(file.getSize());
                    fileEntityRepository.save(fileEntity);
                })
                .exceptionally(throwable -> {
                    throw new IllegalStateException("Failed to save file", throwable);
                });
    }

    @Override
    public String save(MultipartFile file, Chat chat) {
        return "";
    }

    private CompletableFuture<Void> uploadFile(MultipartFile file) {
        return CompletableFuture.runAsync(() -> {
            try {
                var inputStream = new ByteArrayInputStream(file.getBytes());
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .stream(inputStream, inputStream.available(), -1)
                                .bucket(AppConstants.MINIO_DEFAULT_BUCKET.name())
                                .build()
                );
            } catch (Exception e) {
                throw new IllegalStateException("Failed to upload file", e);
            }
        });
    }

}
