package com.mealtracker.exceptions;

import com.mealtracker.payloads.Error;

public class AuthorizationAppException extends AppException {
    private static final int API_ACCESS_DENIED  = 40300;
    private static final int NOT_RESOURCE_OWNER = 40301;

    private AuthorizationAppException(int code, String message) {
        super(code, message);
    }

    public static Error apiAccessDenied() {
        return Error.of(API_ACCESS_DENIED, "You are not allowed to use the api");
    }

    public static AuthorizationAppException notResourceOwner() {
        return new AuthorizationAppException(NOT_RESOURCE_OWNER, "You are not allowed to operate on the given resource");
    }
}
