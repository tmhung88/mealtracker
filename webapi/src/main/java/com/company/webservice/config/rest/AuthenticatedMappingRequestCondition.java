package com.company.webservice.config.rest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

public class AuthenticatedMappingRequestCondition implements RequestCondition<AuthenticatedMappingRequestCondition> {

    @Override
    public AuthenticatedMappingRequestCondition combine(AuthenticatedMappingRequestCondition other) {
        return this;
    }

    @Override
    public AuthenticatedMappingRequestCondition getMatchingCondition(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = authentication == null || authentication instanceof AnonymousAuthenticationToken;
        return isAnonymous ? null : this;
    }

    @Override
    public int compareTo(AuthenticatedMappingRequestCondition other, HttpServletRequest request) {
        return 0;
    }
}
