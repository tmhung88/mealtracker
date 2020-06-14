package com.company.webservice.validation;

import com.company.webservice.validation.LocalTimeFormat;
import com.company.webservice.validation.LocalTimeFormatValidator;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalTimeFormatValidatorTest {

    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    public void isValid_InputMissing_AllowNull_ExpectValid() {
        assertThat(validator(true).isValid(null, context)).isTrue();
    }

    @Test
    public void isValid_InputMissing_NotAllowNull_ExpectInvalid() {
        assertThat(validator(false).isValid(null, context)).isFalse();
    }

    @Test
    public void isValid_InputBadDateFormat_ExpectInvalid() {
        assertThat(validator(false).isValid("00-05", context)).isFalse();
    }

    @Test
    public void isValid_InputValidDateFormat_ExpectValid() {
        assertThat(validator(false).isValid("06:50", context)).isTrue();
    }

    private LocalTimeFormatValidator validator(boolean nullable) {
        var constraintAnnotation = Mockito.mock(LocalTimeFormat.class);
        when(constraintAnnotation.nullable()).thenReturn(nullable);

        var validator = new LocalTimeFormatValidator();
        validator.initialize(constraintAnnotation);
        return validator;
    }
}
