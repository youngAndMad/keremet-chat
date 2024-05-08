package danekerscode.keremetchat.config;

import danekerscode.keremetchat.security.CustomUserDetailsService;
import danekerscode.keremetchat.security.oauth2.CustomOAuth2AuthorizationRequestResolver;
import danekerscode.keremetchat.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import danekerscode.keremetchat.security.oauth2.JdbcClientRegistrationRepository;
import danekerscode.keremetchat.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment env;

    private static final String[] publicEndpoints = {
            "/error",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/user/status/{userId}"
    };

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
    JdbcClientRegistrationRepository clientRegistrationRepository(
            JdbcOperations jdbcOperations,
            OAuth2ClientProperties oAuth2ClientProperties
    ) {
        var propertiesMapper = new OAuth2ClientPropertiesMapper(oAuth2ClientProperties);
        var clientRegistrations = propertiesMapper.asClientRegistrations().values();
        var clientRegistrationMap = clientRegistrations.stream()
                .collect(Collectors.groupingBy(c -> this.getProviderNameByRegistrationId(c.getRegistrationId())));
        return new JdbcClientRegistrationRepository(jdbcOperations, clientRegistrationMap);
    }

    @Bean
    OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        return new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository,"/oauth2/authorization");
    }


    @Bean
    SecurityFilterChain oauth2FilterChain(
            HttpSecurity http,
            OidcUserService customOidcUserService,
            OAuth2AuthenticationSuccessHandler authenticationSuccessHandler,
            HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository,
            OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/api/v1/client-registration").permitAll()
                                .requestMatchers(publicEndpoints).permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                                .successHandler(authenticationSuccessHandler)
                                .permitAll()
                                .authorizationEndpoint(
                                        authEndpoint -> authEndpoint
                                                .authorizationRequestResolver(oAuth2AuthorizationRequestResolver)
                                                .authorizationRequestRepository(authorizationRequestRepository)
                                )
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
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    private CommonOAuth2Provider getProviderNameByRegistrationId(String registrationId) {
        var provider = env.getProperty("spring.security.oauth2.client.registration." + registrationId + ".common-provider-type", CommonOAuth2Provider.class);

        if (provider == null) {
            throw new IllegalArgumentException("Unknown provider: " + registrationId);
        }

        return provider;
    }
}

