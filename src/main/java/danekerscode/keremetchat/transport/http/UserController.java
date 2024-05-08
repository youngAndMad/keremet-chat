package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.core.annotation.FetchUserContext;
import danekerscode.keremetchat.model.dto.request.UsersCriteria;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Delete user by id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{userId}")
    @FetchUserContext(checkPersonalAccess = true)
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId, UserContextHolder.getContext());
    }

    @Operation(summary = "Deactivate user by id")
    @FetchUserContext(checkPersonalAccess = true)
    @PatchMapping("{userId}/deactivate")
    void deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId, UserContextHolder.getContext());
    }

    @GetMapping
    @Operation(summary = "Filter and paginate users")
    @PreAuthorize("hasAnyRole('ROLE_APPLICATION_MANAGER','ROLE_APPLICATION_ROOT_ADMIN')")
    Page<UserResponseDto> filerUsers(
            @ModelAttribute UsersCriteria criteria,
            @RequestParam(required = false, defaultValue = "0") @Positive int page,
            @RequestParam(required = false, defaultValue = "5") @Min(5) @Max(100) int pageSize
    ) {
        return userService.filterUsers(criteria, PageRequest.of(page, pageSize));
    }

}
