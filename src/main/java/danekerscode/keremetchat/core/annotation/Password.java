package danekerscode.keremetchat.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.*;

@Constraint(validatedBy = Password.PasswordValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Password {
    String message() default "Password is not in the correct format length should be between 8 and 30 characters.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    int min() default 8;

    int max() default 30;

    class PasswordValidator implements ConstraintValidator<Password, String> {
        private int min;
        private int max;

        @Override
        public void initialize(Password constraintAnnotation) {
            this.min = constraintAnnotation.min();
            this.max = constraintAnnotation.max();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && value.length() >= min && value.length() <= max;
        }
    }
}
