package com.mealtracker.exceptions;

import com.mealtracker.payloads.Error;

public abstract class AppException extends RuntimeException {
    private final Error error;

    AppException(int code, String message) {
        super(message);
        this.error = Error.of(code, message);
    }

    AppException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
