package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.common.annotation.FetchUserContext;
import danekerscode.keremetchat.context.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.service.AuthService;
import danekerscode.keremetchat.utils.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static danekerscode.keremetchat.common.AppConstants.ACCESS_TOKEN;
import static danekerscode.keremetchat.common.AppConstants.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/auth")
public class AuthController {

    @Value("${app.jwt.expiration}")
    private Integer jwtExpiration;
    private final AuthService authService;


    @PostMapping("register")
    @Operation(description = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User registered")
    })
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody RegistrationRequest request) {
        authService.register(request);
    }

    @GetMapping("me")
    @FetchUserContext
    @Operation(description = "Get current user", responses = {
            @ApiResponse(responseCode = "200", description = "Current user")
    })
    User me() {
        return UserContextHolder.getContext();
    }

    @PostMapping("confirm-email")
    @Operation(description = "Confirm email", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email confirmed. New tokens are set in cookies accessToken and refreshToken"
            )
    })
    User confirmEmail(
            @RequestBody EmailConfirmationRequest request,
            HttpServletResponse response
    ) {
        var authResponse = authService.confirmEmail(request);

        CookieUtils.addCookie(response, ACCESS_TOKEN, authResponse.token().accessToken(), jwtExpiration);
        CookieUtils.addCookie(response, REFRESH_TOKEN, authResponse.token().refreshToken(), jwtExpiration);

        return authResponse.user();
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
    User login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var authResponse = authService.login(loginRequest);

        CookieUtils.addCookie(response, ACCESS_TOKEN, authResponse.token().accessToken(), jwtExpiration);
        CookieUtils.addCookie(response, REFRESH_TOKEN, authResponse.token().refreshToken(), jwtExpiration);

        return authResponse.user();
    }

    @PostMapping("logout")
    @Operation(description = "Logout", responses = {
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    @FetchUserContext
    void logout(
            HttpSession session,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        authService.logout();
        session.invalidate();

        CookieUtils.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
    }

    @PatchMapping("reset-password")
    @FetchUserContext
    @Operation(description = "Reset password", responses = {
            @ApiResponse(responseCode = "200", description = "Password reset")
    })
    User resetPassword(
            @RequestBody ResetPasswordRequest request,
            HttpServletResponse response
    ) {
        var authResponse = authService.resetPassword(request, UserContextHolder.getContext());

        CookieUtils.addCookie(response, ACCESS_TOKEN, authResponse.token().accessToken(), jwtExpiration);
        CookieUtils.addCookie(response, REFRESH_TOKEN, authResponse.token().refreshToken(), jwtExpiration);

        return authResponse.user();
    }
}
