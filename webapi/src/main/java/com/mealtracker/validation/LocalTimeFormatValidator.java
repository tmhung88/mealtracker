package com.mealtracker.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class LocalTimeFormatValidator implements ConstraintValidator<LocalTimeFormat, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        try {
            LocalTime.parse(input);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
