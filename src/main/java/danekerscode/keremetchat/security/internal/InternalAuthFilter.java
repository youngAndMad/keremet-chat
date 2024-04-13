package danekerscode.keremetchat.security.internal;

import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static danekerscode.keremetchat.common.AppConstants.ACCESS_TOKEN;
import static danekerscode.keremetchat.common.AppConstants.INTERNAL_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class InternalAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt;

        var authToken = CookieUtils.getCookie(request, ACCESS_TOKEN);

        if (authToken.isEmpty() || !StringUtils.hasText(authToken.get().getValue()) ||
                !authToken.get().getValue().startsWith(INTERNAL_TOKEN_PREFIX) ||
                !jwtService.validateJwt(jwt = authToken.get().getValue().substring(9))) {
            filterChain.doFilter(request, response);
            return;
        }

        var principal = jwtService.getUsernameFromJwt(jwt);
        var authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority(jwtService.getRoleFromJwt(jwt)))
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        userRepository.findByEmail(principal)
                .ifPresent(UserContextHolder::setContext);

        filterChain.doFilter(request, response);
    }
}