package danekerscode.keremetchat.utils;

import danekerscode.keremetchat.model.dto.response.DownloadFileResponse;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class HttpUtils {

    public static ResponseEntity<InputStreamResource> writeFileInputStreame(
            DownloadFileResponse downloadFileResponse
    ) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFileResponse.fileEntity().getFileName())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(downloadFileResponse.inputStreamResource());
    }
}
