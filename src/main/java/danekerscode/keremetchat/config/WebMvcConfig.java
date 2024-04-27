package danekerscode.keremetchat.config;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.common.interceptor.LoggingInterceptor;
import danekerscode.keremetchat.config.properties.AppProperties;
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

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns(AppConstants.LOGGING_PATH_PATTERN.getValue());

        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    CorsFilter corsFilter(AppProperties appProperties) {
        var cors = appProperties.getCors();

        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(cors.getAllowCredentials());
        corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
        corsConfiguration.setAllowedMethods(cors.getAllowedMethods());
        corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}
