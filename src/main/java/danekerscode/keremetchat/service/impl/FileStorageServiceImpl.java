package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.exception.FileProcessException;
import danekerscode.keremetchat.repository.FileEntityRepository;
import danekerscode.keremetchat.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileEntityRepository fileEntityRepository;
    private final MinioClient minioClient;


    @Override
    public FileEntity save(MultipartFile file, FileEntitySource source, String target) {
        try {
            var inputStream = new ByteArrayInputStream(file.getBytes());

            var minioObjectPath = getMinioObjectPath(AppConstants.MINIO_DEFAULT_BUCKET.name(), file, source, target);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(AppConstants.MINIO_DEFAULT_BUCKET.name())
                            .object(minioObjectPath)
                            .build()
            );

            var fileEntity = new FileEntity();
            fileEntity.setFileName(file.getName());
            fileEntity.setSource(source);
            fileEntity.setSize(file.getBytes().length);
            fileEntity.setExtension(getFileExtension(file));
            fileEntity.setTarget(target);
            fileEntity.setPath(minioObjectPath);

            return fileEntityRepository.save(fileEntity);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }

    @Override
    public void deleteFile(Long fileEntityId) {
        var fileEntityExists = fileEntityRepository.existsById(fileEntityId);

        if (!fileEntityExists) {
            throw new EntityNotFoundException(FileEntity.class, fileEntityId);
        }

        fileEntityRepository.deleteById(fileEntityId);
    }

    @Override
    public FileEntity findById(Long fileEntityId) {
        return fileEntityRepository.findByID(fileEntityId);
    }

    private String getMinioObjectPath(
            String bucketName,
            MultipartFile file,
            FileEntitySource source,
            String target
    ) {
        return bucketName
                .concat("/")
                .concat(source.getPath())
                .concat("/")
                .concat(target)
                .concat(file.getName());
    }

    private String getFileExtension(MultipartFile file) {
        var dotIndex = file.getName().lastIndexOf(AppConstants.DOT.getValue());

        return dotIndex == -1 ? "" : file.getName().substring(dotIndex);
    }


}
