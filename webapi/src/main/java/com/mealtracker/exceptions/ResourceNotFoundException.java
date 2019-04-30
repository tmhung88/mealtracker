package com.mealtracker.exceptions;

public class ResourceNotFoundException extends AppException {
    private static final int NOT_FOUND = 40400;

    public ResourceNotFoundException(String message) {
        super(NOT_FOUND, message);
    }

}
