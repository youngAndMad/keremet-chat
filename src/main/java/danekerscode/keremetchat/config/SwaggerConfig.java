package danekerscode.keremetchat.config;

import danekerscode.keremetchat.config.properties.AppProperties;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI(AppProperties appProperties){
        return new OpenAPI()
                .info(appProperties.getOpenApi().getInfo());
    }
}
