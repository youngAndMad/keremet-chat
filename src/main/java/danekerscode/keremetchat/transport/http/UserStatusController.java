package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.service.UserStatusService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/status")
public class UserStatusController {

    private final UserStatusService userStatusService;

    @GetMapping("{userId}")
    UserActivity getUserActivity(
            @PathVariable @NotNull @Positive Long userId
    ) {
        return this.userStatusService.getUserActivity(userId);
    }

}
