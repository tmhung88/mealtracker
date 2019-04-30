package com.mealtracker.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
    private final int code;
    private final String message;
    private final List<ErrorField> errorFields;

    public static Error of(int code, String message) {
        return new Error(code, message, null);
    }

    public static Error of(int code, String message, List<ErrorField> errorFields) {
        return new Error(code, message, errorFields);
    }
}
