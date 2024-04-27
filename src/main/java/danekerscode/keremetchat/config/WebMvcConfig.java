package danekerscode.keremetchat.config;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.common.LoggingInterceptor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowCredentials}")
    private boolean allowCredentials;

    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Value("${cors.allowedMethods}")
    private String[] allowedMethods;

    @Value("${cors.allowedHeaders}")
    private String[] allowedHeaders;


    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns(AppConstants.LOGGING_PATH_PATTERN.getValue());

        WebMvcConfigurer.super.addInterceptors(registry);
    }

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
