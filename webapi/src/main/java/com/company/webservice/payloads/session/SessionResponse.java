package com.company.webservice.payloads.session;

import com.company.webservice.services.session.AccessToken;
import com.company.webservice.payloads.SuccessEnvelop;
import lombok.Value;

@Value
public class SessionResponse {
    private final String accessToken;
    private final String tokenType;

    public static SuccessEnvelop<SessionResponse> envelop(AccessToken accessToken) {
        return new SuccessEnvelop<>(new SessionResponse(accessToken.getToken(), accessToken.getType()));
    }
}
