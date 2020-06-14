package com.company.webservice.domains;

import java.util.Arrays;
import java.util.List;

public enum Role {
    REGULAR_USER(Arrays.asList(Privilege.MY_MEALS)),
    USER_MANAGER(Arrays.asList(Privilege.MY_MEALS, Privilege.USER_MANAGEMENT)),
    ADMIN(Arrays.asList(Privilege.MY_MEALS, Privilege.USER_MANAGEMENT, Privilege.MEAL_MANAGEMENT)),
    ;

    private final List<Privilege> privileges;

    Role(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}

