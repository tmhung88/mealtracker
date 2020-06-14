package com.company.webservice.exceptions;

import com.company.webservice.payloads.Error;

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
