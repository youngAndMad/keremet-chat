package danekerscode.keremetchat.config.properties;

import io.swagger.v3.oas.models.info.Info;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

    private OpenApiConfig openApi;
    private WebsocketConfig websocket;
    private MinioConfig minio;

    @Data
    public static class OpenApiConfig{
        private Info info;
    }

    @Data
    public static class WebsocketConfig{
        private String[] allowedOrigins;
    }

    @Data
    public static class MinioConfig{
        private String url;
        private String accessKey;
        private String secretKey;
    }
}
