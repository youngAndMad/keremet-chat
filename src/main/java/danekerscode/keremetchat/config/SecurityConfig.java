package danekerscode.keremetchat.config;

import danekerscode.keremetchat.security.CustomUserDetailsService;
import danekerscode.keremetchat.security.internal.InternalAuthFilter;
import danekerscode.keremetchat.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import danekerscode.keremetchat.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private static final List<String> publicEndpoints = new ArrayList<>() {{
        add("/error");
        add("/swagger-ui/**");
        add("/v3/api-docs/**");
        add("/.well-known/**");
    }};

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            AuthenticationProvider daoAuthenticationProvider
    )
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain oauth2FilterChain(
            HttpSecurity http,
            OidcUserService customOidcUserService,
            OAuth2AuthenticationSuccessHandler successHandler,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/error",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**", "/oauth2/**").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo ->
                                        userInfo.oidcUserService(customOidcUserService))
                                .successHandler(successHandler
                                )
                                .authorizationEndpoint(authEndpoint ->
                                        authEndpoint.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                                )
                );

        return http.build();
    }

    @Bean
    @Order(1)
    SecurityFilterChain bearerFilterChain(
            HttpSecurity http,
            InternalAuthFilter internalAuthFilter
    ) throws Exception {

        RequestMatcher bearerRequestMatcher = request -> publicEndpoints.contains(request.getServletPath()) ||
                (StringUtils.hasText(request.getHeader("Authorization")) &&
                        request.getHeader("Authorization").startsWith("Internal "));

        return http
                .securityMatcher(bearerRequestMatcher)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(internalAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(publicEndpoints.toArray(new String[0])).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    AuthenticationProvider daoAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
    }
}