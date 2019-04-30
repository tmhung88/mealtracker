package com.mealtracker.exceptions;

import com.mealtracker.payloads.Error;

public class ResourceNotFoundException extends RuntimeException {
    private static final int NOT_FOUND = 40400;

    private final Error error;

    public static ResourceNotFoundException notFound(String message) {
        return new ResourceNotFoundException(message);
    }

    public ResourceNotFoundException(String message) {
        super();
        this.error = new Error(NOT_FOUND, message);
    }

    public Error getError() {
        return error;
    }
}
