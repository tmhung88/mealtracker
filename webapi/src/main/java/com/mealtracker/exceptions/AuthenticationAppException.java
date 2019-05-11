package com.mealtracker.exceptions;

import com.mealtracker.security.jwt.JwtValidationException;

public class AuthenticationAppException extends AppException {
    private static final int MISSING_TOKEN = 40100;
    private static final int INVALID_JWT_TOKEN = 40101;
    private static final int USERNAME_NOTFOUND = 40102;
    private static final int INVALID_PASSWORD = 40103;
    private static final int ACCOUNT_DELETED = 40104;

    AuthenticationAppException(int code, String message) {
        super(code, message);
    }


    public static AuthenticationAppException missingToken() {
        return new AuthenticationAppException(MISSING_TOKEN, "You need to an access token to use the api");
    }

    public static AuthenticationAppException invalidJwtToken(JwtValidationException ex) {
        return new AuthenticationAppException(INVALID_JWT_TOKEN, ex.getMessage());
    }

    public static AuthenticationAppException usernameNotFound() {
        return new AuthenticationAppException(USERNAME_NOTFOUND, "There isn't an account for this username");
    }

    public static AuthenticationAppException invalidPassword() {
        return new AuthenticationAppException(INVALID_PASSWORD, "Invalid Password");
    }

    public static AuthenticationAppException accountDeleted() {
        return new AuthenticationAppException(ACCOUNT_DELETED, "Your account have been deleted. Please contact our supports if you have any question");
    }
}
