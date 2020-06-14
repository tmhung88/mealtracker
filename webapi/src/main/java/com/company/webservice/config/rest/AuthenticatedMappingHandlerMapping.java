package com.company.webservice.config.rest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class AuthenticatedMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        var authenticatedMappingAnnotation = AnnotationUtils.findAnnotation(handlerType, AuthenticatedMapping.class);
        return authenticatedMappingAnnotation == null ? null : new AuthenticatedMappingRequestCondition();
    }
}
