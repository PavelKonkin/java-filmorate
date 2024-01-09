package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterDateValidator implements ConstraintValidator<AfterDate, LocalDate> {
    private String specificDate;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        specificDate = constraintAnnotation.specificDate();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate providedDate = LocalDate.parse(specificDate);

        return value.isAfter(providedDate);
    }
}

