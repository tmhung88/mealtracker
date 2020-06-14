package com.company.webservice;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public enum TestError {


    BAD_SPECIFIC_INPUT(40001, "%s"),

    AUTHENTICATION_MISSING_TOKEN(40100, "You need to an access token to use the api"),

    AUTHORIZATION_API_ACCESS_DENIED(40300, "You are not allowed to use the api"),

    API_NOT_FOUND(40400, "The given %s does not exist"),
    RESOURCE_DATA_NOT_IN_DB(40401, "The given %s does not exist"),
    ;

    private static final String ERROR_RESPONSE_TEMPLATE = "{'error':{'code':%s,'message':'%s'}}";
    private static final Map<Integer, Supplier<ResultMatcher>> STATUS_MATCHER_MAPPING = buildStatusMatcherMapping();

    private final int code;
    private final String messageTemplate;

    TestError(int code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public ResultMatcher json(Object... params) {
        Supplier<ResultMatcher> a = () -> status().isBadRequest();

        String errorMessage = String.format(messageTemplate, params);
        return content().json(String.format(ERROR_RESPONSE_TEMPLATE, code, errorMessage));
    }

    public ResultMatcher httpStatus() {

        var statusMatcherSupplier = STATUS_MATCHER_MAPPING.get(getHttpStatusCode());
        if (statusMatcherSupplier == null) {
            throw new RuntimeException(String.format("Please define status matcher for this enum %s", this.name()));
        }
        return statusMatcherSupplier.get();
    }

    private int getHttpStatusCode() {
        return code / 100;
    }


    private static Map<Integer, Supplier<ResultMatcher>> buildStatusMatcherMapping() {
        var mapping = new HashMap<Integer, Supplier<ResultMatcher>>();
        mapping.put(400, () -> status().isBadRequest());
        mapping.put(401, () -> status().isUnauthorized());
        mapping.put(403, () -> status().isForbidden());
        mapping.put(404, () -> status().isNotFound());
        return mapping;
    }
}
