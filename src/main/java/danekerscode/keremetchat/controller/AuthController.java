package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.model.dto.request.EmailConfirmationRequest;
import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.response.TokenResponse;
import danekerscode.keremetchat.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
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
            @ApiResponse(responseCode = "200", description = "Email confirmed")
    })
    TokenResponse confirmEmail(
            @RequestBody @Validated EmailConfirmationRequest request
    ) {
        return authService.confirmEmail(request);
    }

    @Operation(description = "Resend otp", responses = {
            @ApiResponse(responseCode = "200", description = "Otp resent")
    })
    @PostMapping("resend-otp")
    void resendOtp(@RequestParam @Email String email) {
        authService.resendOtp(email);
    }


}
