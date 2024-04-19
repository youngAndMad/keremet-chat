package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.service.UserService;
import danekerscode.keremetchat.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final UserStatusService userStatusService;
    private final UserService userService;

    @GetMapping("status/{userId}")
    UserActivity getUserActivity(
            @PathVariable @NotNull @Positive Long userId
    ) {
        return this.userStatusService.getUserActivity(userId);
    }

    @Operation(
            security = @SecurityRequirement(name = "ROLE_APPLICATION_ROOT_ADMIN"),
            summary = "Delete user by id",
            responses = {
                    @ApiResponse(responseCode = "204" , description = "User deleted")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_APPLICATION_ROOT_ADMIN')")
    void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
}
