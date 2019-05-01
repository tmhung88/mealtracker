package com.mealtracker.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValidStringValidator implements ConstraintValidator<ValidString, String> {

    private List<String> acceptableValues;

    @Override
    public void initialize(ValidString constraintAnnotation) {
        acceptableValues = Arrays.asList(constraintAnnotation.values());
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        return acceptableValues.contains(input);
    }
}
