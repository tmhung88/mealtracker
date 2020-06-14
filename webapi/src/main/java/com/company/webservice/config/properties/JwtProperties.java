package com.company.webservice.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtProperties {

    private String secretKey;
    private int expirationInMs;
}
