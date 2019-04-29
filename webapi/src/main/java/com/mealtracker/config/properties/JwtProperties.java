package com.mealtracker.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.jwt")
@Getter @Setter
public class JwtProperties {

    private String secretKey;
    private int expirationInMs;
}
