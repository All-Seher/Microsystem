package web.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsPresentValidation.class)
public @interface IsPresent {
    String message() default "Некорректное имя пользователя";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
