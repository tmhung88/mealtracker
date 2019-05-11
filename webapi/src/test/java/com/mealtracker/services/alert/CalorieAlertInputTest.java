package com.mealtracker.services.alert;

import com.mealtracker.ValidatorProvider;
import org.junit.Test;

import javax.validation.Validator;

import static com.mealtracker.assertions.AppAssertions.assertThat;

public class CalorieAlertInputTest {

    private final Validator validator = ValidatorProvider.getValidator();

    @Test
    public void input_NullDate_ExpectNotNullViolation() {
        var violations = validator.validate(new CalorieAlertInput());
        assertThat(violations).violateNotNull("date");
    }

    @Test
    public void input_BadDateFormat_ExpectLocalDateFormatViolation() {
        var input = new CalorieAlertInput();
        input.setDate("1925/15/125");
        var violations = validator.validate(input);
        assertThat(violations).violateLocalDateFormat("date");
    }

}
