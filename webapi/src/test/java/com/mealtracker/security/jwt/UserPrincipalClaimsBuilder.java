package com.mealtracker.security.jwt;

import com.mealtracker.domains.Privilege;
import com.mealtracker.domains.Role;
import io.jsonwebtoken.Claims;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserPrincipalClaimsBuilder {
    private Long id;
    private String email;
    private Role role;
    private List<Privilege> privileges;
    private String fullName;


    public UserPrincipalClaimsBuilder id(long id) {
        this.id = id;
        return this;
    }

    public UserPrincipalClaimsBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserPrincipalClaimsBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public UserPrincipalClaimsBuilder privileges(Privilege... privileges) {
        this.privileges = Arrays.asList(privileges);
        return this;
    }

    public UserPrincipalClaimsBuilder fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Claims build() {
        var privilegeList = privileges.stream().map(Privilege::name).collect(Collectors.toList());

        var claims = Mockito.mock(Claims.class);
        when(claims.get(eq("id"), eq(Long.class))).thenReturn(id);
        when(claims.get(eq("email"), eq(String.class))).thenReturn(email);
        when(claims.get(eq("role"), eq(String.class))).thenReturn(role.name());
        when(claims.get(eq("privileges"), eq(List.class))).thenReturn(privilegeList);
        when(claims.get(eq("fullName"), eq(String.class))).thenReturn(fullName);
        return claims;
    }

}
