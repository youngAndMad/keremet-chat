package danekerscode.keremetchat.utils;

import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.model.enums.FileEntitySource;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class FileUtils {

    public static String getMinioObjectPath(
            @NonNull String bucketName,
            @NonNull MultipartFile file,
            @NonNull FileEntitySource source,
            @NonNull String target
    ) {
        Assert.hasLength(bucketName,"Not empty bucket name is required");
        Assert.hasLength(target,"Not empty target name is required");
        Assert.notNull(source, "Not null file source is required");
        Assert.notNull(file, "Not null file is required");


        return bucketName
                .concat("/")
                .concat(source.getPath())
                .concat("/")
                .concat(target)
                .concat("/")
                .concat(file.getName());
    }

    public static String getFileExtension(@NonNull MultipartFile file) {
        Assert.notNull(file, "Not null file is required");

        var dotIndex = file.getName().lastIndexOf(AppConstant.DOT.getValue());
        return dotIndex == -1 ? AppConstant.EMPTY_STRING.getValue() : file.getName().substring(dotIndex + 1);
    }
}
