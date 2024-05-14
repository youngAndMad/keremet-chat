package danekerscode.keremetchat.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateGroupChatRequest(
        @NotEmpty String name,
        @NotEmpty List<Long> inviteMembers,
        MultipartFile avatar
) {
}
