package com.mealtracker.exceptions;

public class InternalException extends AppException {
    private static final int INTERNAL_SERVER_CODE = 50000;

    public InternalException(String message) {
        super(INTERNAL_SERVER_CODE, message);
    }

}
