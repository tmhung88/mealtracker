package com.mealtracker.payloads;

import lombok.Value;

@Value
public class SuccessEnvelop<D> {
    private final D data;
}
