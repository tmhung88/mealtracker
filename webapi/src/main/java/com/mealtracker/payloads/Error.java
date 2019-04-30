package com.mealtracker.payloads;

import lombok.Value;

@Value
public class Error {
    private final int code;
    private final String message;
}
