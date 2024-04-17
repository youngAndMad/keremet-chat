package danekerscode.keremetchat.config;

import danekerscode.keremetchat.security.CustomUserDetailsService;
import danekerscode.keremetchat.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import danekerscode.keremetchat.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
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
        add("/api/v1/auth/register");
        add("/api/v1/auth/login");
    }};

    @Bean
    AuthenticationManager authenticationManager(
            HttpSecurity http,
            AuthenticationProvider daoAuthenticationProvider
    )
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider)
                .build();
    }

    @Bean
//    @Order(1)
    SecurityFilterChain oauth2FilterChain(
            HttpSecurity http,
            OidcUserService customOidcUserService,
            OAuth2AuthenticationSuccessHandler successHandler,
            HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(publicEndpoints.toArray(new String[0])).permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(getAuthenticationEntryPoint()))
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                                .successHandler(successHandler)
                                .permitAll()
                                .authorizationEndpoint(authEndpoint -> authEndpoint.authorizationRequestRepository(authorizationRequestRepository))
                )
                /*
                 * If you request POST /logout, then it will perform the following default operations using a series of LogoutHandlers:
                 * Invalidate the HTTP session (SecurityContextLogoutHandler)
                 * Clear the SecurityContextHolderStrategy (SecurityContextLogoutHandler)
                 * Clear the SecurityContextRepository (SecurityContextLogoutHandler)
                 * Clean up any RememberMe authentication (TokenRememberMeServices / PersistentTokenRememberMeServices)
                 * Clear out any saved CSRF token (CsrfLogoutHandler)
                 * Fire a LogoutSuccessEvent (LogoutSuccessEventPublishingLogoutHandler)
                 * Once completed, then it will exercise its default LogoutSuccessHandler which redirects to /login?logout.
                 */
                .logout(logoutSettings -> logoutSettings
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .logoutUrl("/api/v1/auth/logout")
                        .permitAll()
                );

        return http.build();
    }


    private static HttpStatusEntryPoint getAuthenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
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

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}

