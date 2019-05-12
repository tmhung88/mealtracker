package com.mealtracker.validation;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValueInListValidatorTest {
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    public void isValid_InputInList_ExpectValid() {
        Assertions.assertThat(validator("ONE", "TWO", "THREE").isValid("TWO", context)).isTrue();
    }

    @Test
    public void isValid_InputNotInList_ExpectInvalid() {
        Assertions.assertThat(validator("1", "2", "3").isValid("5", context)).isFalse();
    }

    private ValueInListValidator validator(String... value) {
        var constraintAnnotation = Mockito.mock(ValueInList.class);
        when(constraintAnnotation.value()).thenReturn(value);

        var validator = new ValueInListValidator();
        validator.initialize(constraintAnnotation);
        return validator;
    }

}
