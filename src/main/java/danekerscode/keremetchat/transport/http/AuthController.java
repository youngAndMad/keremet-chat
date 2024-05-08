package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.core.annotation.FetchUserContext;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.model.dto.request.LoginRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.request.ResetPasswordRequest;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.exception.Oauth2ProcessingException;
import danekerscode.keremetchat.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    UserResponseDto register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("register/manager")
    @PreAuthorize("hasRole('ROLE_APPLICATION_ROOT_ADMIN')")
    @Operation(description = "Register a new application manager", responses = {
            @ApiResponse(responseCode = "201", description = "Manager registered")
    })
    UserResponseDto registerManager(
            @RequestBody RegistrationRequest request
    ){
        return authService.registerManager(request);
    }

    @GetMapping("me")
    @FetchUserContext
    @Operation(description = "Get current user", responses = {
            @ApiResponse(responseCode = "200", description = "Current user")
    })
    UserResponseDto me() {
        return authService.getCurrentUser();
    }

    @PostMapping("login")
    @Operation(description = "Login", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful")
    })
    UserResponseDto login(
            @RequestBody @Validated LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.login(loginRequest, request, response);
    }

    @GetMapping("login")
    String test(){
        throw new Oauth2ProcessingException("err");
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
