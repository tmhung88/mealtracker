package com.mealtracker.payloads;

import lombok.Value;

@Value
public class MetaSuccessEnvelop<D, M> {

    private final D data;

    private final M metaData;
}
