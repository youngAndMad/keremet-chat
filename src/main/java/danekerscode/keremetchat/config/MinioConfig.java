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
        var minioClient = MinioClient.builder()
                .endpoint(appProperties.getMinio().getUrl())
                .credentials(
                        appProperties.getMinio().getAccessKey(),
                        appProperties.getMinio().getSecretKey()
                )
                .build();

        log.info("Minio client created: Url: {}", appProperties.getMinio().getUrl());
        return minioClient;
    }
}
