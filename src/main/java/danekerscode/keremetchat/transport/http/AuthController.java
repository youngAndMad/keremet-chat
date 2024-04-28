package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.core.annotation.FetchUserContext;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth")
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    @Operation(description = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User registered")
    })
    @ResponseStatus(HttpStatus.CREATED)
    User register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
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
    User login(
            @RequestBody @Validated LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.login(loginRequest, request, response);
    }

    @PostMapping("reset-password")
    @Operation(description = "Reset password", responses = {
            @ApiResponse(responseCode = "200", description = "Password reset")
    })
    @FetchUserContext
    void resetPassword(@RequestBody @Validated ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
    }
}
