package com.mealtracker.exceptions;

import com.mealtracker.domains.Role;
import com.mealtracker.payloads.Error;
import com.mealtracker.payloads.ErrorField;

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

    public static BadRequestAppException invalidDateTimeRange(String fromFieldName, String toFieldName) {
        var errorMessage = String.format("%s must be before %s", fromFieldName, toFieldName);
        return new BadRequestAppException(SPECIFIC_BAD_INPUT, errorMessage);
    }

    public static BadRequestAppException deleteYourself() {
        var errorMessage = "You cannot delete your own account. Please ask your peer to perform";
        return new BadRequestAppException(SPECIFIC_BAD_INPUT, errorMessage);
    }

    public static BadRequestAppException interactSuperiorRole(Role superiorRole) {
        var errorMessage = "You are not allowed to interact with " + superiorRole.name();
        return new BadRequestAppException(SPECIFIC_BAD_INPUT, errorMessage);
    }
}
