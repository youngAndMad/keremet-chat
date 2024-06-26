package danekerscode.keremetchat.config;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.repository.JdbcClientRegistrationRepository;
import danekerscode.keremetchat.repository.impl.JdbcClientRegistrationRepositoryImpl;
import danekerscode.keremetchat.security.oauth2.CustomOAuth2AuthorizationRequestResolver;
import danekerscode.keremetchat.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import danekerscode.keremetchat.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Optional;
import java.util.stream.Collectors;

import static danekerscode.keremetchat.core.AppConstant.ENV_COMMON_OAUTH2_PROVIDER_PLACEHOLDER_PATTERN;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    public static final HttpStatusEntryPoint AUTHENTICATION_ENTRY_POINT = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    private final Environment env;

    private static final String[] publicEndpoints = {
            "/error",
            "/actuator/**",
            "/oauth2/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/email/verify/{token}",
            "/api/v1/user/status/{userId}",
    };

    @Bean
    AuthenticationManager authenticationManager(
            @NotNull HttpSecurity http,
            UserDetailsService customUserDetailsService
    )
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(createAuthenticationManagerForUserDetailsService(customUserDetailsService))
                .build();
    }

    @Bean
    JdbcClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties,
            JdbcClient jdbcClient
    ) {
        var propertiesMapper = new OAuth2ClientPropertiesMapper(oAuth2ClientProperties);
        var clientRegistrations = propertiesMapper.asClientRegistrations().values();
        var clientRegistrationMap = clientRegistrations.stream()
                .collect(Collectors.groupingBy(c -> this.getProviderNameByRegistrationId(c.getRegistrationId())));
        return new JdbcClientRegistrationRepositoryImpl(jdbcClient, clientRegistrationMap);
    }

    @Bean
    OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        return new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository, AppConstant.AUTHORIZATION_REQUEST_BASE_URL.getValue());
    }

    @Bean
    @Order(3)
    SecurityFilterChain oauth2FilterChain(
            HttpSecurity http,
            OidcUserService customOidcUserService,
            OAuth2AuthenticationSuccessHandler authenticationSuccessHandler,
            HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository,
            OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver,
            @Qualifier("authenticationManager") AuthenticationManager authenticationManager
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
                                                        .authorizationRequestRepository(authorizationRequestRepository)
//                                                .authorizationRequestResolver(oAuth2AuthorizationRequestResolver) // todo fix problem with ant matcher
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
                .authenticationManager(authenticationManager)
                .logout(logoutSettings -> logoutSettings
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .logoutUrl("/api/v1/auth/logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    UserDetailsService basicInMemoryUserDetailsService(
            AppProperties appProperties,
            PasswordEncoder passwordEncoder
    ) {
        var basicAuth = appProperties.getSecurity().getBasicAuth();
        var user = User.withUsername(basicAuth.getUsername())
                .password(passwordEncoder.encode(basicAuth.getPassword()))
                .roles(basicAuth.getRole())
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @Order(1)
    SecurityFilterChain actuatorFilterChain(
            HttpSecurity http,
            UserDetailsService basicInMemoryUserDetailsService,
            AppProperties appProperties
    ) throws Exception {
        var authenticationManager = http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(createAuthenticationManagerForUserDetailsService(basicInMemoryUserDetailsService)).build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .securityMatcher(request -> request.getRequestURI().startsWith("/actuator"))
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().hasRole(appProperties.getSecurity().getBasicAuth().getRole())
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(AUTHENTICATION_ENTRY_POINT));

        return http.build();
    }

//    @Bean
//    @Order(2)
    SecurityFilterChain swaggerFilterChain(
            HttpSecurity http,
            UserDetailsService basicInMemoryUserDetailsService,
            AppProperties appProperties
    ) throws Exception {
        var authenticationManager = http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(createAuthenticationManagerForUserDetailsService(basicInMemoryUserDetailsService)).build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .securityMatcher(request -> request.getRequestURI().startsWith("/swagger"))
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole(appProperties.getSecurity().getBasicAuth().getRole())
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(AUTHENTICATION_ENTRY_POINT));

        return http.build();
    }

    private AuthenticationProvider createAuthenticationManagerForUserDetailsService(
            UserDetailsService userDetailsService
    ) {
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
        return Optional
                .ofNullable(env.getProperty(ENV_COMMON_OAUTH2_PROVIDER_PLACEHOLDER_PATTERN.getValue().formatted(registrationId), CommonOAuth2Provider.class))
                .orElseThrow(() -> new IllegalArgumentException("Unknown provider: " + registrationId));
    }
}

