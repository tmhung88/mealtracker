package com.mealtracker.services.user;

import com.mealtracker.ValidatorProvider;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.validation.Validator;

import static com.mealtracker.assertions.AppAssertions.assertThat;

public class ListUsersInputTest {
    private final Validator validator = ValidatorProvider.getValidator();

    @Test
    public void getOrderBy_dailyCalorieLimit_ExpectEntityPathReturned() {
        var input = new ListUsersInput();
        input.setOrderBy("dailyCalorieLimit");
        Assertions.assertThat(input.getOrderBy()).isEqualTo("userSettings.dailyCalorieLimit");
    }

    @Test
    public void input_UnsupportedSortableColumn_ExpectViolation() {
        var badInput = validInput();
        badInput.setOrderBy("Privilege");
        assertThat(validator.validate(badInput)).violateValueInList("orderBy", "id", "email", "fullName", "role", "dailyCalorieLimit");
    }


    private ListUsersInput validInput() {
        var input = new ListUsersInput();
        input.setOrderBy("id");
        input.setOrder("desc");
        return input;
    }
}
