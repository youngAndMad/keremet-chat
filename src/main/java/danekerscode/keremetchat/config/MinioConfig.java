package danekerscode.keremetchat.config;

import danekerscode.keremetchat.config.properties.AppProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MinioConfig {

    @Bean
    MinioClient minioClient(
            AppProperties appProperties
    ){
        var minio = appProperties.getMinio();

        var minioClient = MinioClient.builder()
                .endpoint(minio.getUrl())
                .credentials(
                        minio.getAccessKey(),
                        minio.getSecretKey()
                )
                .build();

        log.info("Minio client created: Url: {}", minio.getUrl());
        return minioClient;
    }
}
