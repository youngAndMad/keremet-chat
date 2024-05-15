package danekerscode.keremetchat.config.properties;

import io.swagger.v3.oas.models.info.Info;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@Validated
public class AppProperties {

    @Valid
    private MailConfig mail;
    @Valid
    private OpenApiConfig openApi;
    @Valid
    private WebsocketConfig websocket;
    @Valid
    private MinioConfig minio;
    @Valid
    private CorsConfig cors;
    @Valid
    private SecurityConfig security;

    @Data
    public static class OpenApiConfig {
        private Info info;
    }

    @Data
    public static class MailConfig{
        @NotEmpty
        private String sender;
        @NotEmpty
        private String verificationLinkPattern;
        @NotNull
        private Duration verificationTokenTtl;
    }

    @Data
    public static class WebsocketConfig {
        private String[] allowedOrigins;
    }

    @Data
    public static class MinioConfig {
        @NotEmpty
        private String url;
        @NotEmpty
        private String accessKey;
        @NotEmpty
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
    public static class SecurityConfig {
        @Valid
        private Admin defaultAdmin;

        @Data
        public static class Admin {
            @NotEmpty
            private String email;
            @NotEmpty
            private String password;
        }
    }
}
