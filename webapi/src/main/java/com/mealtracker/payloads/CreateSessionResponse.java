package com.mealtracker.payloads;

import lombok.Value;

@Value
public class CreateSessionResponse {
    private final String accessToken;
    private final String tokenType;

    public static CreateSessionResponse bearerToken(String accessToken) {
        return new CreateSessionResponse(accessToken, "Bearer");
    }
}
