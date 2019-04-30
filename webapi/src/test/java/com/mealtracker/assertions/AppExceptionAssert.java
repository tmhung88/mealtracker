package com.mealtracker.assertions;

import com.mealtracker.exceptions.AppException;
import com.mealtracker.payloads.Error;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert;

public class AppExceptionAssert extends AbstractThrowableAssert<AppExceptionAssert, AppException> {
    private Error actual;
    public AppExceptionAssert(AppException e) {
        super(e, AppExceptionAssert.class);
        actual = e.getError();
    }

    public static AppException catchThrowable(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        try {
            shouldRaiseThrowable.call();
        } catch (AppException throwable) {
            return throwable;
        } catch (Throwable throwable) {
            return null;
        }
        return null;
    }

    public AppExceptionAssert hasError(int code, String message) {
        if (!message.equals(actual.getMessage())) {
            failWithMessage("Expect error message to be <%s> but was <%s>", message, actual.getMessage());
        }

        if (!(code == actual.getCode())) {
            failWithMessage("Expect error code to be <%s> but was <%s>", code, actual.getCode());
        }
        return this;
    }

}
