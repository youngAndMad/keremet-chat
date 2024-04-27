package danekerscode.keremetchat.utils;

import danekerscode.keremetchat.common.AppConstants;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class FileUtils {

    public static String getMinioObjectPath(
            String bucketName,
            MultipartFile file,
            FileEntitySource source,
            String target
    ) {
        return bucketName
                .concat("/")
                .concat(source.getPath())
                .concat("/")
                .concat(target)
                .concat(file.getName());
    }

    public static String getFileExtension(MultipartFile file) {
        var dotIndex = file.getName().lastIndexOf(AppConstants.DOT.getValue());

        return dotIndex == -1 ? "" : file.getName().substring(dotIndex);
    }
}
