package com.mealtracker.exceptions;

public class InternalAppException extends AppException {
    private static final int INTERNAL_SERVER_CODE = 50000;

    private InternalAppException(String message) {
        super(INTERNAL_SERVER_CODE, message);
    }

    public static InternalAppException unexpectException(String uniqueExceptionId) {
        var errorMessage = String.format("Oops! Please send us this id %s. We love to know what magic happened to you!", uniqueExceptionId);
        return new InternalAppException(errorMessage);
    }

}
