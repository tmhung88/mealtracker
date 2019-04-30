package com.mealtracker.payloads;

import lombok.Value;

@Value
public class SuccessEnvelop<T> {
    public final T data;
}
