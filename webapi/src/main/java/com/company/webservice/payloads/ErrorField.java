package com.company.webservice.payloads;

import lombok.Value;

@Value
public class ErrorField {
    private final String name;
    private final String message;
}
