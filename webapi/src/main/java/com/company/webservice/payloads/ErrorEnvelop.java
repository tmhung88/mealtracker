package com.company.webservice.payloads;

import com.company.webservice.exceptions.AppException;
import lombok.Getter;

@Getter
public class ErrorEnvelop {
    private final Error error;

    public ErrorEnvelop(Error error) {
        this.error = error;
    }

    public ErrorEnvelop(AppException ex) {
        this.error = ex.getError();
    }

}
