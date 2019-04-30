package com.mealtracker.payloads;

import lombok.Getter;

@Getter
public class ErrorEnvelop {
    private final Error error;

    public ErrorEnvelop(Error error) {
        this.error = error;
    }

}
