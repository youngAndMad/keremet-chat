package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.model.dto.response.CountResponse;
import danekerscode.keremetchat.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("httpUserStatusController")
@RequiredArgsConstructor
@Tag(name="User status")
@RequestMapping("api/v1/user/status")
public class UserStatusController {

    private final UserStatusService userStatusService;

    @GetMapping("{userId}")
    UserActivity getUserActivity(
            @PathVariable @NotNull @Positive Long userId
    ) {
        return this.userStatusService.getUserActivity(userId);
    }

    @GetMapping("online/count")
    @PreAuthorize("hasAnyRole('ROLE_APPLICATION_MANAGER','ROLE_APPLICATION_ROOT_ADMIN')")
    @Operation(description = "Get count of online users")
    CountResponse getOnlineUsersCount() {
        return new CountResponse(this.userStatusService.getOnlineUsersCount());
    }

}
