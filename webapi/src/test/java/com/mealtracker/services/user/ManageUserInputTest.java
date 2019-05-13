package com.mealtracker.services.user;

import com.mealtracker.ValidatorProvider;
import com.mealtracker.validation.OnAdd;
import com.mealtracker.validation.OnUpdate;
import org.junit.Test;

import javax.validation.Validator;

import static com.mealtracker.assertions.AppAssertions.assertThat;

public class ManageUserInputTest {
    private final Validator validator = ValidatorProvider.getValidator();

    @Test
    public void input_RoleMissing_ExpectNotNullViolation() {
        var input = validInput();
        input.setRole(null);

        assertThat(validator.validate(input, OnAdd.class)).violateNotNull("role");
        assertThat(validator.validate(input, OnUpdate.class)).violateNotNull("role");
    }

    @Test
    public void input_RoleUndefinedValue_ExpectValueInListViolation() {
        var input = validInput();
        input.setRole("USER");

        assertThat(validator.validate(input, OnAdd.class)).violateValueInList("role", "REGULAR_USER", "USER_MANAGER", "ADMIN");
        assertThat(validator.validate(input, OnUpdate.class)).violateValueInList("role", "REGULAR_USER", "USER_MANAGER", "ADMIN");
    }

    @Test
    public void input_CaloriesMissing_ExpectNotNullViolation() {
        var input = validInput();
        input.setDailyCalorieLimit(null);

        assertThat(validator.validate(input, OnAdd.class)).violateNotNull("dailyCalorieLimit");
        assertThat(validator.validate(input, OnUpdate.class)).violateNotNull("dailyCalorieLimit");
    }

    ManageUserInput validInput() {
        var input = new ManageUserInput();
        input.setEmail("email@abc.com");
        input.setFullName("full name");
        input.setDailyCalorieLimit(400);
        input.setRole("ADMIN");
        input.setPassword("emptyfornow");
        return input;
    }
}
