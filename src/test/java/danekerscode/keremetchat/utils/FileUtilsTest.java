package danekerscode.keremetchat.utils;

import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void getFileExtension_FileWithExtension_ReturnsExtension() {
        var file = new MockMultipartFile("some.txt", new byte[0]);

        var fileExtension = FileUtils.getFileExtension(file);

        assertEquals(fileExtension, "txt");
    }

    @Test
    void getFileExtension_FileWithoutExtension_ReturnsEmptyString() {
        var file = new MockMultipartFile("some_file_without_extension", new byte[0]);

        var fileExtension = FileUtils.getFileExtension(file);

        assertEquals(fileExtension, AppConstant.EMPTY_STRING.getValue());
    }

    @Test
    void getMinioObjectPath_ValidInputs_ReturnsObjectPath(){
        var file = new MockMultipartFile("some.png", new byte[0]);

        var minioObjectPath = FileUtils.getMinioObjectPath(
                AppConstant.MINIO_DEFAULT_BUCKET.getValue(),
                file,
                FileEntitySource.CHAT_AVATAR,
                "1"
        );

        var expectedObjectPath = AppConstant.MINIO_DEFAULT_BUCKET.getValue()
                .concat("/")
                .concat(FileEntitySource.CHAT_AVATAR.getPath())
                .concat("/")
                .concat("1")
                .concat("/")
                .concat(file.getName());

        assertEquals(expectedObjectPath,minioObjectPath);
    }

    @Test
    void getMinioObjectPath_NullInput_ThrowsException(){
        var file = (MockMultipartFile) null;

        assertThrows(IllegalArgumentException.class, () -> FileUtils.getFileExtension(file));
    }



}