package com.mealtracker.services.usersettings;

import com.mealtracker.ValidatorProvider;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.validation.Validator;

import static com.mealtracker.assertions.AppAssertions.assertThat;

public class MySettingsInputTest {

    private final Validator validator = ValidatorProvider.getValidator();

    @Test
    public void isDailyCalorieLimitPatched_CalorieMissing_ExpectNoUpdateNeed() {
        Assertions.assertThat(settings(null).isDailyCalorieLimitPatched()).isFalse();
    }

    @Test
    public void isDailyCalorieLimitPatched_CalorieAvailable_ExpectNeedUpdate() {
        Assertions.assertThat(settings(456).isDailyCalorieLimitPatched()).isTrue();
    }


    @Test
    public void input_CalorieTooSmall_ExpectMinViolation() {
        assertThat(validator.validate(settings(-1))).violateMin("dailyCalorieLimit", 0);
    }

    @Test
    public void input_CalorieTooBig_ExpectMaxViolation() {
        assertThat(validator.validate(settings(50001))).violateMax("dailyCalorieLimit", 50000);
    }

    private MySettingsInput settings(Integer calorieLimit) {
        var input = new MySettingsInput();
        input.setDailyCalorieLimit(calorieLimit);
        return input;
    }
}
