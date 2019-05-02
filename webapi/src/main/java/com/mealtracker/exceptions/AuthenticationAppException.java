package com.mealtracker.exceptions;

import com.mealtracker.security.jwt.JwtValidationException;

public class AuthenticationAppException extends AppException {
    private static final int MISSING_TOKEN = 40100;
    private static final int INVALID_JWT_TOKEN = 40101;

    AuthenticationAppException(int code, String message) {
        super(code, message);
    }


    public static AuthenticationAppException missingToken() {
        return new AuthenticationAppException(MISSING_TOKEN, "You need to an access token to use the api");
    }

    public static AuthenticationAppException invalidJwtToken(JwtValidationException ex) {
        return new AuthenticationAppException(INVALID_JWT_TOKEN, ex.getMessage());
    }

}
