package com.mealtracker.exceptions;

public class BadRequestException extends AppException {
    private static final int BadInput = 40000;

    public BadRequestException(String message) {
        super(BadInput, message);
    }
}
