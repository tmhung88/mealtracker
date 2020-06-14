package com.company.webservice.domains;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static com.company.webservice.domains.Privilege.MEAL_MANAGEMENT;
import static com.company.webservice.domains.Privilege.MY_MEALS;
import static com.company.webservice.domains.Privilege.USER_MANAGEMENT;
import static com.company.webservice.domains.Role.ADMIN;
import static com.company.webservice.domains.Role.REGULAR_USER;
import static com.company.webservice.domains.Role.USER_MANAGER;

public class RoleTest {
    @Test
    public void regularUser_GetPrivileges_ExpectMyMeals() {
        Assertions.assertThat(REGULAR_USER.getPrivileges()).containsExactly(MY_MEALS);
    }

    @Test
    public void userManager_GetPrivileges_ExpectMyMealsAndUserManagement() {
        Assertions.assertThat(USER_MANAGER.getPrivileges()).containsExactlyInAnyOrder(MY_MEALS, USER_MANAGEMENT);
    }

    @Test
    public void userManager_GetPrivileges_ExpectMyMealsAndUserManagementAndMealManagement() {
        Assertions.assertThat(ADMIN.getPrivileges()).containsExactlyInAnyOrder(MY_MEALS, USER_MANAGEMENT, MEAL_MANAGEMENT);
    }
}
