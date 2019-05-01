package com.mealtracker.exceptions;

import com.mealtracker.payloads.Error;

public class InternalAppException extends AppException {
    private static final int INTERNAL_SERVER_CODE = 50000;

    private InternalAppException(String message) {
        super(INTERNAL_SERVER_CODE, message);
    }

    public static Error unexpectException(String uniqueExceptionId) {
        var errorMessage = String.format("Oops! Please send us this id %s. We love to know what magic happened to you!", uniqueExceptionId);
        return Error.of(INTERNAL_SERVER_CODE, errorMessage);
    }

}
