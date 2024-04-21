package danekerscode.keremetchat.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface AvailableForRootOrOwner {
    String sourceName() default "userId";
}
