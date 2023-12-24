package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterCinemaBirthdayValidator implements ConstraintValidator<AfterCinemaBirthday, LocalDate> {
    private String specificDate;

    @Override
    public void initialize(AfterCinemaBirthday constraintAnnotation) {
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

