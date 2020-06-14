package com.company.webservice.security;

import com.company.webservice.domains.Ownable;
import com.company.webservice.domains.Privilege;
import com.company.webservice.domains.Role;
import com.company.webservice.domains.User;
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

    public boolean isOwner(Ownable resource) {
        return id.equals(resource.getOwner().getId());
    }

    public User toUser() {
        var user = new User();
        user.setId(id);
        return user;
    }
}
