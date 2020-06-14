package com.company.webservice.services.user;

import com.company.webservice.assertions.AppAssertions;
import com.company.webservice.ValidatorProvider;
import com.company.webservice.services.session.SessionInput;
import org.junit.Test;

import javax.validation.Validator;

public class SessionInputTest {

    private final Validator validator = ValidatorProvider.getValidator();


    @Test
    public void input_EmailNull_ExpectNotNullViolation() {
        var input = validInput();
        input.setEmail(null);
        AppAssertions.assertThat(validator.validate(input)).violateNotNull("email");
    }

    @Test
    public void input_EmailBadFormat_ExpectBadEmailFormatViolation() {
        var input = validInput();
        input.setEmail("abc");
        AppAssertions.assertThat(validator.validate(input)).violateEmailFormat("email");
    }

    @Test
    public void input_PasswordNull_ExpectNotNullViolation() {
        var input = validInput();
        input.setPassword(null);
        AppAssertions.assertThat(validator.validate(input)).violateNotNull("password");
    }


    private SessionInput validInput() {
        var input = new SessionInput();
        input.setEmail("abc@gmail.com");
        input.setPassword("helloworld");
        return input;
    }
}
