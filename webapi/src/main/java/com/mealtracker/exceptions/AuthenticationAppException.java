package com.mealtracker.exceptions;

import com.mealtracker.payloads.Error;

public class AuthenticationAppException extends AppException {
    private static final int MISSING_TOKEN = 40100;

    private AuthenticationAppException(int code, String message) {
        super(code, message);
    }


    public static Error missingToken() {
        return Error.of(MISSING_TOKEN, "You need to an access token to use the api");
    }

}
