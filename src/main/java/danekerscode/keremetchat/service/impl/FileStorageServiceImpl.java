package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.model.dto.KeyPair;
import danekerscode.keremetchat.model.dto.response.DownloadFileResponse;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.FileProcessException;
import danekerscode.keremetchat.repository.FileEntityRepository;
import danekerscode.keremetchat.service.FileStorageService;
import danekerscode.keremetchat.utils.FileUtils;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final FileEntityRepository fileEntityRepository;
    private final MinioClient minioClient;

    @Override
    public FileEntity save(MultipartFile file, FileEntitySource source, String target) {
        try {
            var inputStream = new ByteArrayInputStream(file.getBytes());

            var minioObjectPath = FileUtils
                    .getMinioObjectPath(AppConstant.MINIO_DEFAULT_BUCKET.getValue(), file, source, target);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(AppConstant.MINIO_DEFAULT_BUCKET.getValue())
                            .object(minioObjectPath)
                            .build()
            );

            var fileEntity = new FileEntity();
            fileEntity.setFileName(file.getName());
            fileEntity.setSource(source);
            fileEntity.setSize(file.getBytes().length);
            fileEntity.setExtension(FileUtils.getFileExtension(file));
            fileEntity.setTarget(target);
            fileEntity.setPath(minioObjectPath);

            return fileEntityRepository.save(fileEntity);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(Long fileEntityId) {
        var fileEntityExists = fileEntityRepository.existsById(fileEntityId);

        if (!fileEntityExists) {
            throw new EntityNotFoundException(FileEntity.class, fileEntityId);
        }

        var file = fileEntityRepository.findByID(fileEntityId);

        fileEntityRepository.deleteById(fileEntityId);

        deleteFileFromMinio(file);
    }

    private void deleteFileFromMinio(FileEntity file) {
        try {
            minioClient.removeObject(
                    io.minio.RemoveObjectArgs.builder()
                            .bucket(AppConstant.MINIO_DEFAULT_BUCKET.getValue())
                            .object(file.getPath())
                            .build()
            );
        } catch (Exception e) {
            throw new FileProcessException(e);
        }

    }

    @Override
    public FileEntity findById(Long fileEntityId) {
        return fileEntityRepository.findByID(fileEntityId);
    }

    @Override
    public CompletableFuture<DownloadFileResponse> downloadFile(
            Long fileEntityId
    ) {
        var fileEntity = fileEntityRepository.findByID(fileEntityId);
        var minioObjectPath = fileEntity.getPath();

        return this.downloadFileByPath(minioObjectPath)
                .thenApply((inputStreamResource -> new DownloadFileResponse(fileEntity, inputStreamResource)));
    }

    @Override
    public CompletableFuture<DownloadFileResponse> downloadFileWithPath(String objectPath) {
        var fileEntity = fileEntityRepository.findByPath(objectPath)
                .orElseThrow(() -> new EntityNotFoundException(FileEntity.class,  KeyPair.of("path", objectPath)));

        return this.downloadFileByPath(fileEntity.getPath())
                .thenApply((inputStreamResource -> new DownloadFileResponse(fileEntity, inputStreamResource)));
    }

    private CompletableFuture<InputStreamResource> downloadFileByPath(
            String objectPath
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var response = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(AppConstant.MINIO_DEFAULT_BUCKET.getValue())
                                .object(objectPath)
                                .build()
                );
                return new InputStreamResource(response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(th -> {
            throw new FileProcessException(th);
        });
    }

}
