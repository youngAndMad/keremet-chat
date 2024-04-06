package danekerscode.keremetchat.authserver.controller;

import danekerscode.keremetchat.authserver.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.authserver.model.dto.response.ApiStatusResponse;
import danekerscode.keremetchat.authserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    ApiStatusResponse register(@RequestBody @Validated RegistrationRequest request){

    }


}
