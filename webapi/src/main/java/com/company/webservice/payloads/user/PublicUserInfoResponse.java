package com.company.webservice.payloads.user;

import com.company.webservice.domains.User;
import com.company.webservice.payloads.SuccessEnvelop;
import lombok.Value;

@Value
public class PublicUserInfoResponse {
    private final String fullName;
    private final String email;

    public static SuccessEnvelop<PublicUserInfoResponse> of(User user) {
        return new SuccessEnvelop<>(new PublicUserInfoResponse(user.getFullName(), user.getEmail()));
    }
}
