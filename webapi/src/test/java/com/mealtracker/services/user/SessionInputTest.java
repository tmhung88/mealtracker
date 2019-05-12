package com.mealtracker.services.user;

import com.mealtracker.ValidatorProvider;
import com.mealtracker.services.session.SessionInput;
import org.junit.Test;

import javax.validation.Validator;

import static com.mealtracker.assertions.AppAssertions.assertThat;

public class SessionInputTest {

    private final Validator validator = ValidatorProvider.getValidator();


    @Test
    public void input_EmailNull_ExpectNotNullViolation() {
        var input = validInput();
        input.setEmail(null);
        assertThat(validator.validate(input)).violateNotNull("email");
    }

    @Test
    public void input_EmailBadFormat_ExpectBadEmailFormatViolation() {
        var input = validInput();
        input.setEmail("abc");
        assertThat(validator.validate(input)).violateEmailFormat("email");
    }

    @Test
    public void input_PasswordNull_ExpectNotNullViolation() {
        var input = validInput();
        input.setPassword(null);
        assertThat(validator.validate(input)).violateNotNull("password");
    }



    private SessionInput validInput() {
        var input = new SessionInput();
        input.setEmail("abc@gmail.com");
        input.setPassword("helloworld");
        return input;
    }
}
