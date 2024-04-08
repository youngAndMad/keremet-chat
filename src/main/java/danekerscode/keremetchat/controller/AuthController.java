package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.utils.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    @Value("${app.jwt.expiration}")
    private Integer jwtExpiration;


    @PostMapping("register")
    @Operation(description = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User registered")
    })
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody @Validated RegistrationRequest request) {
        authService.register(request);
    }

    @GetMapping("me")
    Authentication me() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("confirm-email")
    @Operation(description = "Confirm email", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email confirmed. New tokens are set in cookies accessToken and refreshToken"
            )
    })
    void confirmEmail(
            @RequestBody @Validated EmailConfirmationRequest request,
            HttpServletResponse response
    ) {
        var tokenResponse = authService.confirmEmail(request);

        CookieUtils.addCookie(response, "accessToken", tokenResponse.accessToken(), jwtExpiration);
        CookieUtils.addCookie(response, "refreshToken", tokenResponse.refreshToken(), jwtExpiration);
    }

    @Operation(description = "Resend otp", responses = {
            @ApiResponse(responseCode = "200", description = "Otp resent")
    })
    @PostMapping("resend-otp")
    void resendOtp(@RequestParam @Email String email) {
        authService.resendOtp(email);
    }

    @PostMapping("login")
    @Operation(description = "Login", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful")
    })
    void login(
            @RequestBody @Validated LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var tokenResponse = authService.login(loginRequest);

        CookieUtils.addCookie(response, "accessToken", tokenResponse.accessToken(), jwtExpiration);
        CookieUtils.addCookie(response, "refreshToken", tokenResponse.refreshToken(), jwtExpiration);
    }

    @PostMapping("logout")
    @Operation(description = "Logout", responses = {
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    void logout(HttpSession session) {
        authService.logout();
        session.invalidate();
    }


}
