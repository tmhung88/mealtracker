package com.mealtracker.exceptions;

import java.util.UUID;

public class ErrorIdGenerator {

    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
