package com.mealtracker.exceptions;

public class ResourceNotFoundAppException extends AppException {
    private static final int RESOURCE_NOT_IN_DB = 40401;

    private ResourceNotFoundAppException(int code, String message) {
        super(code, message);
    }

    public static ResourceNotFoundAppException resourceNotInDb(ResourceName resourceName) {
        return new ResourceNotFoundAppException(RESOURCE_NOT_IN_DB, String.format("The given %s does not exist", resourceName.getName()));
    }

}
