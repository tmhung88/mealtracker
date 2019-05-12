package com.mealtracker.services.user;

import com.mealtracker.exceptions.AuthorizationAppException;
import com.mealtracker.security.CurrentUser;

import java.util.Arrays;
import java.util.List;

public class UserManagementServiceResolver {
    private final List<UserManagementService> userManagementServices;

    public UserManagementServiceResolver(UserManagementService... userManagementServices) {
        this.userManagementServices = Arrays.asList(userManagementServices);
    }

    public UserManagementService resolve(CurrentUser currentUser) {
        return userManagementServices.stream().filter(service -> service.canUsedBy(currentUser)).findFirst()
                .orElseThrow(AuthorizationAppException::apiAccessDenied);
    }
}
