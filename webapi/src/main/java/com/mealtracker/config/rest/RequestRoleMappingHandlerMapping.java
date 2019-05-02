package com.mealtracker.config.rest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class RequestRoleMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        var roleRequestMappingAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestRoleMapping.class);
        if (roleRequestMappingAnnotation == null) {
            return null;
        }
        return new RequestRoleMappingRequestCondition(roleRequestMappingAnnotation.value());
    }
}
