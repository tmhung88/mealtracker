package com.mealtracker.assertions;

import com.mealtracker.exceptions.AppException;
import org.assertj.core.api.ThrowableAssert;

import static com.mealtracker.assertions.AppExceptionAssert.catchThrowable;


public class AppAssertions {

    public static AppExceptionAssert assertThatThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return assertThat(catchThrowable(shouldRaiseThrowable));
    }

    private static AppExceptionAssert assertThat(AppException ex) {
        return new AppExceptionAssert(ex);
    }

}
