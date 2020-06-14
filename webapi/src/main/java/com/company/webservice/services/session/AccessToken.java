package com.company.webservice.services.session;

import lombok.Value;

@Value
public class AccessToken {
    private static final String BEARER_TYPE = "Bearer";
    private final String token;
    private final String type;


    public static AccessToken jwt(String token) {
        return new AccessToken(token, BEARER_TYPE);
    }

}
