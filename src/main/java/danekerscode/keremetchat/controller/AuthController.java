package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.common.annotation.FetchUserContext;
import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("register")
    @Operation(description = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User registered")
    })
    @ResponseStatus(HttpStatus.CREATED)
    User register(@RequestBody RegistrationRequest request) {
        authService.register(request);

        return authService.login(new LoginRequest(request.email(), request.password()));
    }

    @GetMapping("me")
    @FetchUserContext
    @Operation(description = "Get current user", responses = {
            @ApiResponse(responseCode = "200", description = "Current user")
    })
    User me() {
        return UserContextHolder.getContext();
    }

    @PostMapping("login")
    @Operation(description = "Login", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful")
    })
    void login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        var passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),loginRequest.password()
        );

        var session = request.getSession(true);
        var authentication = authenticationManager.authenticate(passwordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        var cookie = new Cookie("JSESSIONID", session.getId());
        response.addCookie(cookie);
    }


}
