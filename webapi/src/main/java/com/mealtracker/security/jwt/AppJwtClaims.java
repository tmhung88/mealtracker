package com.mealtracker.security.jwt;

import com.mealtracker.domains.Privilege;
import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.security.UserPrincipal;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppJwtClaims {
    private final Long id;
    private final String email;
    private final Role role;
    private final List<Privilege> privileges;
    private final String fullName;

    public AppJwtClaims(UserPrincipal userPrincipal) {
        id = userPrincipal.getId();
        email = userPrincipal.getEmail();
        role = userPrincipal.getRole();
        privileges = userPrincipal.getPrivileges();
        fullName = userPrincipal.getFullName();
    }

    public AppJwtClaims(Claims claims) {
        id = claims.get("id", Long.class);
        email = claims.get("email", String.class);
        role = Role.valueOf(claims.get("role", String.class));
        List<String> privilegeList = (List<String>) claims.get("privileges", List.class);
        privileges = privilegeList.stream().map(Privilege::valueOf).collect(Collectors.toList());
        fullName = claims.get("fullName", String.class);
    }

    public Map<String, Object> toMap() {
        var claims = new HashMap<String, Object>();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("role", role);
        claims.put("privileges", privileges);
        claims.put("fullName", fullName);
        return claims;
    }

    public UserPrincipal toUserPrincipal() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setRole(role);
        user.setFullName(fullName);
        return new UserPrincipal(user);
    }

}
