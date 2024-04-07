package danekerscode.keremetchat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class WebMvcConfig {

    @Value("${cors.allowCredentials}")
    private boolean allowCredentials;

    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Value("${cors.allowedMethods}")
    private String[] allowedMethods;

    @Value("${cors.allowedHeaders}")
    private String[] allowedHeaders;


    @Bean
    CorsFilter corsFilter() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(allowCredentials);
        corsConfiguration.setAllowedOrigins(Arrays.stream(allowedOrigins).collect(Collectors.toList()));
        corsConfiguration.setAllowedMethods(Arrays.stream(allowedMethods).collect(Collectors.toList()));
        corsConfiguration.setAllowedHeaders(Arrays.stream(allowedHeaders).collect(Collectors.toList()));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
