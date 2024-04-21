package danekerscode.keremetchat.model.modify.message;

import java.time.LocalDateTime;
import java.util.List;

public record MessageSaveRequest(
        Long chatId,
        Long senderId,
        String content,
        LocalDateTime sentAt,
        List<Long> savedFileIdList,
        Long parentId
) {
}
