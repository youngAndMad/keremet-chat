package danekerscode.keremetchat.context.runner;

import danekerscode.keremetchat.core.AppConstant;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioBucketInitializer implements ApplicationRunner {

    private final MinioClient minioClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var bucketName = AppConstant.MINIO_DEFAULT_BUCKET.getValue();

        var bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (bucketExists){
            log.info("Bucket already exists: {}", bucketName);
            return;
        }

        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        log.info("Bucket created: {}", bucketName);
    }
}
