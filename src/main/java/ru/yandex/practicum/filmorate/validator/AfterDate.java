package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AfterDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterDate {
    String message() default "Дата релиза должна быть позже даты выхода первого фильма";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String specificDate();
}
