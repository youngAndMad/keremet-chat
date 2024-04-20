package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.model.dto.request.ClientRegistrationRequest;
import danekerscode.keremetchat.service.ClientRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client-registration")
@PreAuthorize("hasRole('ROLE_APPLICATION_ROOT_ADMIN')")
@Tag(name = "Client registration")
public class ClientRegistrationController {

    private final ClientRegistrationService clientRegistrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create client registration")
    void createClientRegistration(
            @RequestBody @Validated ClientRegistrationRequest clientRegistrationRequest
    ) {
        clientRegistrationService.save(clientRegistrationRequest);
    }


    @DeleteMapping("{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete client registration by id")
    void deleteClientRegistration(
            @PathVariable String clientId
    ) {
        clientRegistrationService.delete(clientId);
    }

    @PutMapping("{clientId}")
    @Operation(summary = "Update client registration")
    void updateClientRegistration(
            @RequestBody @Validated ClientRegistrationRequest clientRegistrationRequest
    ) {
        clientRegistrationService.update(clientRegistrationRequest);
    }

    @GetMapping("{clientId}")
    @Operation(summary = "Get client registration by id")
    ClientRegistration getClientRegistration(
            @PathVariable String clientId
    ) {
        return clientRegistrationService.findByRegistrationId(clientId);
    }

    @GetMapping
    @Operation(summary = "Get all client registrations")
    Iterator<ClientRegistration> getAllClientRegistrations() {
        return clientRegistrationService.findAll();
    }
}
