package com.company.webservice.config.rest;

import com.company.webservice.security.CurrentUser;
import com.company.webservice.security.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        if (supportsParameter(parameter)) {
            var principal = webRequest.getUserPrincipal();
            var userPrincipal = (UserPrincipal) ((Authentication) principal).getPrincipal();
            return userPrincipal.toCurrentUser();
        } else {
            return WebArgumentResolver.UNRESOLVED;
        }
    }
}
