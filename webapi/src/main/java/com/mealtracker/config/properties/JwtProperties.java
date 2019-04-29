package com.mealtracker.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.jwt")
public class JwtProperties {
    private String secretKey;
    private int expirationInMs;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getExpirationInMs() {
        return expirationInMs;
    }

    public void setExpirationInMs(int expirationInMs) {
        this.expirationInMs = expirationInMs;
    }
}
