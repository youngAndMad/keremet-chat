package danekerscode.keremetchat.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface FetchUserContext {

    AccessCheck accessCheck() default @AccessCheck;

    boolean checkPersonalAccess() default false;

    @interface AccessCheck {
        boolean availableForAdmin() default true;

        String parameterName() default "userId";
    }

}
