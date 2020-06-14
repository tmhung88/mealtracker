package com.company.webservice.validation;

import com.company.webservice.validation.LocalDateFormat;
import com.company.webservice.validation.LocalDateFormatValidator;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalDateFormatValidatorTest {

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
        assertThat(validator(false).isValid("2018/05/06", context)).isFalse();
    }

    @Test
    public void isValid_InputValidDateFormat_ExpectValid() {
        assertThat(validator(false).isValid("2018-05-06", context)).isTrue();
    }

    private LocalDateFormatValidator validator(boolean nullable) {
        var constraintAnnotation = Mockito.mock(LocalDateFormat.class);
        when(constraintAnnotation.nullable()).thenReturn(nullable);

        var validator = new LocalDateFormatValidator();
        validator.initialize(constraintAnnotation);
        return validator;
    }
}
