package com.mealtracker.payloads;

import lombok.Value;

@Value
public class ErrorField {
    private final String name;
    private final String message;
}
