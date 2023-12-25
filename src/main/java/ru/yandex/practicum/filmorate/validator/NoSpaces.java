package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "^(?=\\S+$).*$", message = "Логин не должен содержать пробелы")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface NoSpaces {
    String message() default "Логин не должен содержать пробелы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
