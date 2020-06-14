package com.company.webservice.exceptions;

import com.company.webservice.payloads.Error;
import com.company.webservice.payloads.ErrorField;

import java.util.List;

public class BadRequestAppException extends AppException {
    private static final int COMMON_BAD_INPUT = 40000;
    private static final int SPECIFIC_BAD_INPUT = 40001;

    private BadRequestAppException(int code, String message) {
        super(code, message);
    }

    public static Error commonBadInputsError(List<ErrorField> errorFields) {
        return Error.of(COMMON_BAD_INPUT, "Bad Input", errorFields);
    }

    public static BadRequestAppException emailTaken(String email) {
        return new BadRequestAppException(SPECIFIC_BAD_INPUT, String.format("Email %s is already taken", email));
    }
}
