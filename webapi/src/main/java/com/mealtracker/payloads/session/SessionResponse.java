package com.mealtracker.payloads.session;

import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.services.session.AccessToken;
import lombok.Value;

@Value
public class SessionResponse {
    private final String accessToken;
    private final String tokenType;

    public static SuccessEnvelop<SessionResponse> envelop(AccessToken accessToken) {
        return new SuccessEnvelop<>(new SessionResponse(accessToken.getToken(), accessToken.getType()));
    }
}
