package com.mealtracker.payloads;

import lombok.Value;

@Value
public class MetaSuccesEnvelop<D, M> {

    private final D data;

    private final M metaData;
}
