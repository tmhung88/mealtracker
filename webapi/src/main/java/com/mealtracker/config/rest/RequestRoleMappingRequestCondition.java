package com.mealtracker.config.rest;

import com.mealtracker.config.rest.RequestRoleMapping.RequestRole;
import com.mealtracker.exceptions.AuthenticationAppException;
import com.mealtracker.security.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

public class RequestRoleMappingRequestCondition implements RequestCondition<RequestRoleMappingRequestCondition> {

    private final RequestRole role;

    public RequestRoleMappingRequestCondition(RequestRole role) {
        this.role = role;
    }

    @Override
    public RequestRoleMappingRequestCondition combine(RequestRoleMappingRequestCondition other) {
        return this;
    }

    @Override
    public RequestRoleMappingRequestCondition getMatchingCondition(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = authentication == null || authentication instanceof AnonymousAuthenticationToken;
        if (isAnonymous) {
            throw AuthenticationAppException.missingToken();
        }

        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        var userHasRole = role.name().equals(userPrincipal.getRole().name());
        if (!userHasRole) {
            return null;
        }
        return this;
    }

    @Override
    public int compareTo(RequestRoleMappingRequestCondition other, HttpServletRequest request) {
        return 0;
    }
}
