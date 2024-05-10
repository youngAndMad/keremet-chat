package danekerscode.keremetchat.config.properties;

import io.swagger.v3.oas.models.info.Info;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

    private OpenApiConfig openApi;
    private WebsocketConfig websocket;
    private MinioConfig minio;
    private CorsConfig cors;
    private SecurityConfig security;

    @Data
    public static class OpenApiConfig {
        private Info info;
    }

    @Data
    public static class WebsocketConfig {
        private String[] allowedOrigins;
    }

    @Data
    public static class MinioConfig {
        private String url;
        private String accessKey;
        private String secretKey;
    }

    @Data
    public static class CorsConfig {
        private Boolean allowCredentials;
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
    }

    @Data
    public static class SecurityConfig{

        private Admin defaultAdmin;

        @Data
        public static class Admin{
            private String email;
            private String password;
        }
    }
}
