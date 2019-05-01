package com.mealtracker.security;

import com.mealtracker.domains.Privilege;
import com.mealtracker.domains.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CurrentUser {
    private final Long id;
    private final String email;
    private final Role role;
    private final List<Privilege> privileges;
    private final String fullName;

    public CurrentUser(UserPrincipal userPrincipal) {
        this.id = userPrincipal.getId();
        this.email = userPrincipal.getEmail();
        this.role = userPrincipal.getRole();
        this.privileges = new ArrayList<>(userPrincipal.getPrivileges());
        this.fullName = userPrincipal.getFullName();
    }
}
