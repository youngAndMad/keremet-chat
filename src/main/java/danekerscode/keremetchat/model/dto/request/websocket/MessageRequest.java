package danekerscode.keremetchat.model.dto.request.websocket;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public record MessageRequest(
        String content,
        List<MultipartFile> files,
        Long parentId
) implements Serializable {
}
