package com.company.webservice.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class LocalTimeFormatValidator implements ConstraintValidator<LocalTimeFormat, String> {
    private boolean nullable;

    @Override
    public void initialize(LocalTimeFormat constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return nullable;
        }

        try {
            LocalTime.parse(input);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
