package danekerscode.keremetchat.model.dto.response;

import danekerscode.keremetchat.model.entity.FileEntity;
import org.springframework.core.io.InputStreamResource;

public record DownloadFileResponse(
        FileEntity fileEntity,
        InputStreamResource inputStreamResource
) {
}
