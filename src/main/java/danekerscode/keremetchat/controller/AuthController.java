package danekerscode.keremetchat.controller;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody @Validated RegistrationRequest request) {
        authService.register(request);
    }

    @GetMapping("me")
    Object me() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
