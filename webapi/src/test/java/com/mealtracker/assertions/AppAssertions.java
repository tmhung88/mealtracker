package com.mealtracker.assertions;

import com.mealtracker.exceptions.AppException;
import org.assertj.core.api.ThrowableAssert;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.mealtracker.assertions.AppExceptionAssert.catchThrowable;


public class AppAssertions {

    public static AppExceptionAssert assertThatThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return assertThat(catchThrowable(shouldRaiseThrowable));
    }

    public static <T> ConstraintViolationAssert<T> assertThat(Set<ConstraintViolation<T>> violations) {
        return new ConstraintViolationAssert(violations);
    }

    private static AppExceptionAssert assertThat(AppException ex) {
        return new AppExceptionAssert(ex);
    }


}
