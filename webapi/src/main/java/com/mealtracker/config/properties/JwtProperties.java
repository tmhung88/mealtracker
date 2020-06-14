package com.mealtracker.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtProperties {

    private String secretKey;
    private int expirationInMs;
}
