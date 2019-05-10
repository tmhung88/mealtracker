package com.mealtracker.payloads.session;

import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Value;

@Value
public class CreateSessionResponse {
    private final String accessToken;
    private final String tokenType;

    public static SuccessEnvelop<CreateSessionResponse> envelop(String accessToken) {
        return new SuccessEnvelop<>(new CreateSessionResponse(accessToken, "Bearer"));
    }
}
