package com.mealtracker.services.meal;

import com.mealtracker.ValidatorProvider;
import com.mealtracker.assertions.AppAssertions;
import org.junit.Test;

import javax.validation.Validator;

public class MyMealInputTest {
    private final Validator validator = ValidatorProvider.getValidator();

    @Test
    public void input_NameMissing_ExpectNotNullViolation() {
        var input = validInput();
        input.setName(null);

        AppAssertions.assertThat(validator.validate(input)).violateNotNull("name");
    }

    @Test
    public void input_NameTooShort_ExpectSizeViolation() {
        var input = validInput();
        input.setName("1");

        AppAssertions.assertThat(validator.validate(input)).violateSize("name", 2, 200);
    }


    @Test
    public void input_CalorieTooSmall_ExpectMinViolation() {
        var input = validInput();
        input.setCalories(0);

        AppAssertions.assertThat(validator.validate(input)).violateMin("calories", 1);
    }

    @Test
    public void input_CalorieTooBig_ExpectMaxViolation() {
        var input = validInput();
        input.setCalories(10001);

        AppAssertions.assertThat(validator.validate(input)).violateMax("calories", 10000);
    }

    @Test
    public void input_ConsumedDateMissing_ExpectNotNullViolation() {
        var input = validInput();
        input.setConsumedDate(null);

        AppAssertions.assertThat(validator.validate(input)).violateNotNull("consumedDate");
    }

    @Test
    public void input_ConsumedDateBadFormat_ExpectLocalDateFormatViolation() {
        var input = validInput();
        input.setConsumedDate("53/24/124");

        AppAssertions.assertThat(validator.validate(input)).violateLocalDateFormat("consumedDate");
    }

    @Test
    public void input_ConsumedTimeBadTimeFOrmat_ExpectBadTimeFormatViolation() {
        var input = validInput();
        input.setConsumedTime("435");

        AppAssertions.assertThat(validator.validate(input)).violateLocalTimeFormat("consumedTime");
    }

    MyMealInput validInput() {
        var input = new MyMealInput();
        input.setName("Pizza Hawai");
        input.setConsumedTime("09:15");
        input.setConsumedDate("2019-05-04");
        input.setCalories(400);
        return input;
    }
}
