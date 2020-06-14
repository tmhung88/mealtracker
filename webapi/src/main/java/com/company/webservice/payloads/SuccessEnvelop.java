package com.company.webservice.payloads;

import lombok.Value;

@Value
public class SuccessEnvelop<D> {
    private final D data;
}
