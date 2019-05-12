package com.mealtracker.services.user;

import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.security.CurrentUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserManagementServiceResolverTest {
    private final UserManagementService adminService = Mockito.mock(UserManagementService.class);
    private final UserManagementService managerService = Mockito.mock(UserManagementService.class);
    private final UserManagementServiceResolver serviceResolver = new UserManagementServiceResolver(adminService, managerService);


    @Test
    public void resolve_ExpectFirstHandlingServiceReturned() {
        var user = currentUser();
        when(adminService.canUsedBy(user)).thenReturn(true);
        when(managerService.canUsedBy(user)).thenReturn(true);

        Assertions.assertThat(serviceResolver.resolve(user)).isEqualTo(adminService);
    }

    @Test
    public void resolve_NoServiceFound_ExpectException() {
        var user = currentUser();
        when(adminService.canUsedBy(user)).thenReturn(false);
        when(managerService.canUsedBy(user)).thenReturn(false);

        AppAssertions.assertThatThrownBy(() -> serviceResolver.resolve(user)).hasError(40300, "You are not allowed to use the api");
    }

    CurrentUser currentUser() {
        return mock(CurrentUser.class);
    }
}
