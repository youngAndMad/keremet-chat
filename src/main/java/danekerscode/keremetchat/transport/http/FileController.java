package danekerscode.keremetchat.transport.http;

import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.service.FileStorageService;
import danekerscode.keremetchat.utils.HttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Tag(name = "Files")
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("download/{id}")
    @Operation(description = "Download file by id")
    CompletableFuture<ResponseEntity<InputStreamResource>> downloadFile(
            @PathVariable Long id
    ) {
        return fileStorageService.downloadFile(id)
                .thenApplyAsync(HttpUtils::writeFileInputStream);
    }

    @GetMapping("download/with-path/{path}")
    @Operation(description = "Download file with path")
    CompletableFuture<ResponseEntity<InputStreamResource>> downloadFileWithPath(
            @PathVariable String path
    ) {
        return fileStorageService.downloadFileWithPath(path)
                .thenApplyAsync(HttpUtils::writeFileInputStream);
    }

    @GetMapping("{id}")
    @Operation(description = "Get file by id")
    FileEntity getFileById(
            @PathVariable Long id
    ) {
        return fileStorageService.findById(id);
    }
}
