package danekerscode.keremetchat.model.notification;

import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.FileEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record MessageNotification(
        String content,
        UserResponseDto sender,
        List<FileEntity> files,
        LocalDateTime sentTime,
        String chatName,
        Long chatId,
        Long parentId
) implements Serializable {
}
