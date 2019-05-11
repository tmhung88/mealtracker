package com.mealtracker.payloads.user;

import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Value;

@Value
public class SessionResponse {
    private final String accessToken;
    private final String tokenType;

    public static SuccessEnvelop<SessionResponse> envelop(String accessToken) {
        return new SuccessEnvelop<>(new SessionResponse(accessToken, "Bearer"));
    }
}
