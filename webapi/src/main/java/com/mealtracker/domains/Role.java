package com.mealtracker.domains;

import java.util.Arrays;
import java.util.List;

import static com.mealtracker.domains.Privilege.MEAL_MANAGEMENT;
import static com.mealtracker.domains.Privilege.MY_MEALS;
import static com.mealtracker.domains.Privilege.USER_MANAGEMENT;

public enum Role {
    REGULAR_USER(Arrays.asList(MY_MEALS)),
    USER_MANAGER(Arrays.asList(MY_MEALS, USER_MANAGEMENT)),
    ADMIN(Arrays.asList(MY_MEALS, USER_MANAGEMENT, MEAL_MANAGEMENT)),
    NO_MY_MEAL(Arrays.asList(USER_MANAGEMENT, MEAL_MANAGEMENT)),
    NO_USER_MANAGEMENT(Arrays.asList(MY_MEALS, MEAL_MANAGEMENT)),
    NO_MEAL_MANAGEMENT(Arrays.asList(MY_MEALS, USER_MANAGEMENT)),
    ;

    private final List<Privilege> privileges;

    Role(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}

