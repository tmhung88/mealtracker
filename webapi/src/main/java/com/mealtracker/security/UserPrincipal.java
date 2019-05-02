package com.mealtracker.security;

import com.mealtracker.domains.Privilege;
import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final Role role;
    private final List<Privilege> privileges;
    private final String fullName;
    private final String getEncryptedPassword;
    private final boolean enabled;


    public static UserPrincipal allDetails(User user) {
        return new UserPrincipal(user.getId(), user.getEmail(), user.getRole(), user.getPrivileges(),
                user.getFullName(), user.getEncryptedPassword(), user.isEnabled());
    }

    public static UserPrincipal jwtClaims(Claims claims) {
        var id = claims.get("id", Long.class);
        var email = claims.get("email", String.class);
        var role = Role.valueOf(claims.get("role", String.class));
        var privilegeList = (List<String>) claims.get("privileges", List.class);
        var privileges = privilegeList.stream().map(Privilege::valueOf).collect(Collectors.toList());
        var fullName = claims.get("fullName", String.class);
        return new UserPrincipal(id, email, role, privileges, fullName, null, true);
    }

    public UserPrincipal(Long id,
                         String email,
                         Role role,
                         List<Privilege> privileges,
                         String fullName, String getEncryptedPassword, boolean enabled) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.privileges = privileges;
        this.fullName = fullName;
        this.getEncryptedPassword = getEncryptedPassword;
        this.enabled = enabled;
    }


    public Map<String, Object> toJwtClaims() {
        var claims = new HashMap<String, Object>();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("role", role);
        claims.put("privileges", privileges);
        claims.put("fullName", fullName);
        return claims;
    }

    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return privileges.stream().map(Privilege::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return getEncryptedPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public CurrentUser toCurrentUser() {
        return new CurrentUser(this);
    }
}
