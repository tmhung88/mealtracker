package com.mealtracker.payloads;

import lombok.Value;

@Value
public class CreateSessionResponse {
    private final String accessToken;
    private final String tokenType = "Bearer";


}
